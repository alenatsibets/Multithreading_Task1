package example.scheduler.task.impl;


import example.scheduler.task.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class DataProcessingTask implements Task {
    private static final Logger logger = LogManager.getLogger();
    private final int[] numbers;

    public DataProcessingTask(int[] numbers) {
        this.numbers = numbers;
    }

    @Override
    public void run() {
        try {
            logger.info("DataProcessingTask started. Processing...");
            TimeUnit.SECONDS.sleep(3); // Simulating processing delay of 3 seconds

            int sum = 0;
            for (int num : numbers) {
                sum += num;
            }

            logger.info("DataProcessingTask completed: Sum of numbers = " + sum);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("DataProcessingTask was interrupted", e);
        }
    }
}
