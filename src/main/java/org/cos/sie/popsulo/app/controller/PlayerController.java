package org.cos.sie.popsulo.app.controller;

import com.github.axet.vget.VGet;
import com.github.axet.vget.info.VGetParser;
import com.github.axet.vget.info.VideoFileInfo;
import com.github.axet.vget.info.VideoInfo;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.app.VGetStatus;
import org.cos.sie.popsulo.app.utils.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.floor;
import static java.lang.String.format;

public class PlayerController
    implements Initializable
{
    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private static final String baseUrl = "https://www.youtube.com/watch?v=";
    private static final double PERCENTS = 100.0;
    private static final int SECONDS_MINUTES_HOURS = 60;

    @FXML private VBox mainPane;
    @FXML private Label timeLabel;
    @FXML private Slider slider;
    @FXML private Label videoIdLabel;
    @FXML private Label titleLabel;
    @FXML private Label authorLabel;
    @FXML private Label dateLabel;
    @FXML private ImageView miniatureImageView;

    private String titleBase;
    private String videoIdBase;
    private String authorBase;
    private String dateBase;
    private StringProperty time;
    private Duration duration;
    private MediaPlayer mediaPlayer;

    @Override public void initialize(URL location, ResourceBundle resources)
    {
        ResourceBundle bundle = ResourceUtils.loadLabelsForDefaultLocale();
        videoIdBase = bundle.getString("labels.player.video.id");
        titleBase = bundle.getString("labels.player.video.title");
        authorBase = bundle.getString("labels.player.author");
        dateBase = bundle.getString("labels.player.video.date");
        mainPane.setVisible(false);
    }

    public void pause()
    {
        mediaPlayer.pause();
    }

    public void play()
    {
        if (mediaPlayer.getCurrentTime().equals(mediaPlayer.getStopTime())) {
            mediaPlayer.seek(mediaPlayer.getStartTime());
        }
        mediaPlayer.play();
    }

    public void updateState(QueryResult result)
        throws URISyntaxException
    {
        miniatureImageView.setImage(result.getMiniature());

        updateMediaPlayer(result);

        duration = mediaPlayer.getMedia().getDuration();
        updateLabels(result);

        slider.valueProperty().addListener(e -> {
            if (slider.isValueChanging()) {
                if (duration != null) {
                    mediaPlayer.seek(duration.multiply(slider.getValue() / PERCENTS));
                }
                updateProgress();
            }
        });

        updateProgress();
        mainPane.setVisible(true);
    }

    private void updateMediaPlayer(QueryResult result)
    {
        String fileUrl = result.getFileUrl();
        if (fileUrl == null) {
            fileUrl = getStreamUrl(result.getVideoId());
        }
        Media media = new Media(fileUrl);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        mediaPlayer.currentTimeProperty().addListener((ChangeListener)(observable, oldValue, newValue) -> {
            updateProgress();
        });

        mediaPlayer.currentTimeProperty().addListener((Observable ob) -> {
            updateProgress();
        });

        mediaPlayer.setOnReady(() -> {
            duration = mediaPlayer.getMedia().getDuration();
            updateProgress();
        });
    }

    private void updateLabels(QueryResult result)
    {
        videoIdLabel.setText(videoIdBase + " : " + result.getVideoId());
        titleLabel.setText(titleBase + " : " + result.getTitle());
        authorLabel.setText(authorBase + " : " + result.getAuthor());
        dateLabel.setText(dateBase + " : " + result.getPublishingDate());
        time = new SimpleStringProperty();
        time.setValue(formatTime(mediaPlayer.getCurrentTime(), duration));
        timeLabel.textProperty().bind(time);
    }

    private void updateProgress() {
        if (duration != null) {
            Platform.runLater(() -> {
                Duration currentTime = mediaPlayer.getCurrentTime();
                time.setValue(formatTime(currentTime, duration));
                slider.setDisable(duration.isUnknown());
                if (!slider.isDisabled() && duration.greaterThan(Duration.ZERO) && !slider.isValueChanging()) {
                    slider.setValue(currentTime.divide(duration).toMillis() * PERCENTS);
                }
            });
        }
    }

    private String getStreamUrl(String videoId)
    {
        try {
            final AtomicBoolean stop = new AtomicBoolean(false);
            URL web = new URL(baseUrl + videoId);
            VGetParser user = VGet.parser(web);

            VideoInfo videoinfo = user.info(web);
            VGet v = new VGet(videoinfo);
            VGetStatus notify = new VGetStatus(videoinfo);
            v.extract(user, stop, notify);

            List<VideoFileInfo> list = videoinfo.getInfo();
            if (list != null) {
                for (VideoFileInfo d : list) {
                    return d.getSource().toString();
                }
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        throw new IllegalStateException("Cannot find stream url");
    }

    private static String formatTime(Duration elapsed, Duration duration)
    {
        int intElapsed = (int)floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (SECONDS_MINUTES_HOURS * SECONDS_MINUTES_HOURS);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * SECONDS_MINUTES_HOURS * SECONDS_MINUTES_HOURS;
        }
        int elapsedMinutes = intElapsed / SECONDS_MINUTES_HOURS;
        int elapsedSeconds = intElapsed - elapsedHours * SECONDS_MINUTES_HOURS * SECONDS_MINUTES_HOURS
            - elapsedMinutes * SECONDS_MINUTES_HOURS;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int)floor(duration.toSeconds());
            int durationHours = intDuration / (SECONDS_MINUTES_HOURS * SECONDS_MINUTES_HOURS);
            if (durationHours > 0) {
                intDuration -= durationHours * SECONDS_MINUTES_HOURS * SECONDS_MINUTES_HOURS;
            }
            int durationMinutes = intDuration / SECONDS_MINUTES_HOURS;
            int durationSeconds = intDuration - durationHours * SECONDS_MINUTES_HOURS * SECONDS_MINUTES_HOURS
                - durationMinutes * SECONDS_MINUTES_HOURS;
            if (durationHours > 0) {
                return format("%d:%02d:%02d/%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds, durationHours,
                    durationMinutes, durationSeconds);
            } else {
                return format("%02d:%02d/%02d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes, durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return format("%02d:%02d", elapsedMinutes, elapsedSeconds);
            }
        }
    }
}
