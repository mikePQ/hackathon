package org.cos.sie.popsulo.app.controller;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.app.utils.ResourceUtils;
import org.cos.sie.popsulo.converter.FormatConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.ResourceBundle;

import static java.lang.Math.floor;
import static java.lang.String.format;

public class PlayerController
    implements Initializable
{
    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

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

        try {
            String path = FormatConverter.class.getResource("testInput.mp4").toURI().toString();
            Image miniatureImage = new Image(getClass().getResource("/icons/mainIcon.png").toString());
            String videoId = "1111";
            String title = "ziemniak";
            String author = "zbyszek";
            Date date = new Date(Instant.now().toEpochMilli());

            QueryResult result = new QueryResult(videoId, title, author, date, miniatureImage, path);
            updateState(result);
        } catch (URISyntaxException e) {
            logger.error("Cannot initialize player controller", e);
        }
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

    void updateState(QueryResult result)
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
    }

    private void updateMediaPlayer(QueryResult result)
    {
        Media media = new Media(result.getFileUrl());
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

    private static final double PERCENTS = 100.0;
    private static final int SECONDS_MINUTES_HOURS = 60;
}
