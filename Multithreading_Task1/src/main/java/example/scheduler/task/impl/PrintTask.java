package example.scheduler.task.impl;

import example.scheduler.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class PrintTask implements Task {
    private static final Logger logger = LogManager.getLogger();
    private final String message;

    public PrintTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            logger.info("PrintTask started: {}", message);

            TimeUnit.MILLISECONDS.sleep(300);

            logger.info("PrintTask completed: {}", message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("PrintTask was interrupted", e);
        }
    }
}
