package example.scheduler.task.impl;

import example.scheduler.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class FileWriteTask implements Task {
    private static final Logger logger = LogManager.getLogger();
    private final String filePath;
    private final String message;

    public FileWriteTask(String filePath, String message) {
        this.filePath = filePath;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            logger.info("FileWriteTask started. Writing to file: {}", filePath);

            TimeUnit.MILLISECONDS.sleep(500);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
                String logMessage = LocalDateTime.now() + " - " + message;
                writer.write(logMessage);
                writer.newLine();
                logger.info("FileWriteTask completed: {}", logMessage);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("FileWriteTask was interrupted", e);
        } catch (IOException e) {
            logger.error("Failed to write to file: {}", filePath, e);
        }
    }
}
