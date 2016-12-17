package org.cos.sie.popsulo.app.utils.timer;

import javafx.concurrent.Task;
import org.cos.sie.popsulo.app.QueryResult;
import org.cos.sie.popsulo.youtubeSearch.SearchQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class TimerService {

	private static final Logger logger = LoggerFactory.getLogger(TimerService.class);

	private static final long DEFAULT_INTERVAL_MILIS = 1000;

	private static TimerService instance;

	private TimerService() {
	}

	private Task lock;

	private String query;
	private SearchQueryService service;
	private Supplier<String> querySupplier;
	private Consumer<List<QueryResult>> guiUpdater;
	private Runnable errorHandler;

	private boolean executeNewQueryAfterCurrentIsFinished = false;
	private boolean queryIsExecuted = false;


	public static TimerService getTimerService() {
		if ( Objects.isNull(instance) ) {
			instance = new TimerService();
		}
		return instance;
	}


	public void executeQuery(SearchQueryService service, Supplier<String> querySupplier, Consumer<List<QueryResult>> guiUpdater, Runnable errorHandler) {
		if ( lock == null || !lock.isRunning() ) {
			lock = new Task() {
				@Override
				protected Object call() throws Exception {
					query = querySupplier.get();
					Thread.sleep(DEFAULT_INTERVAL_MILIS);
					return null;
				}
			};

			lock.setOnSucceeded(event -> {
				queryIsExecuted = true;
				String newQuery = querySupplier.get();
				if ( !Objects.equals(newQuery, query) ) {
					query = newQuery;
				}
				performQuery(service, guiUpdater, errorHandler);
				if ( executeNewQueryAfterCurrentIsFinished ) {
					executeNewQueryAfterCurrentIsFinished = false;
					performQuery(service, guiUpdater, errorHandler);
				}
				queryIsExecuted = false;
			});

			Thread asynchThread = new Thread(lock);
			asynchThread.setDaemon(true);
			asynchThread.start();
		} else {
			if ( queryIsExecuted ) {
				this.service = service;
				this.querySupplier = querySupplier;
				this.guiUpdater = guiUpdater;
				this.errorHandler = errorHandler;
				this.executeNewQueryAfterCurrentIsFinished = true;
			}
		}
	}

	private void performQuery(SearchQueryService service, Consumer<List<QueryResult>> guiUpdater, Runnable errorHandler) {
		logger.info("Querying youtube ");
		List<QueryResult> results;
		try {
			results = service.queryYoutube(query);
		} catch ( IOException e ) {
			errorHandler.run();
			return;
		}
		guiUpdater.accept(results);
	}
}
