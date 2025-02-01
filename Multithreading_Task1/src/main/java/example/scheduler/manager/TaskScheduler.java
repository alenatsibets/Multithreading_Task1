package example.scheduler.manager;

import example.scheduler.manager.reader.FileReaderUtility;
import example.scheduler.state.TaskState;
import example.scheduler.task.ScheduledTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

public class TaskScheduler {
    private static final Logger logger = LogManager.getLogger();
    private final ScheduledExecutorService executorService;
    private final BlockingQueue<ScheduledTask> taskQueue;

    public TaskScheduler(int poolSize) {
        this.executorService = Executors.newScheduledThreadPool(poolSize);
        this.taskQueue = new LinkedBlockingQueue<>();
    }

    public void addTask(ScheduledTask scheduledTask) {
        try {
            taskQueue.put(scheduledTask);
            scheduleTask(scheduledTask);
            logger.info("Task added to queue: {}", scheduledTask.getTask().getClass().getSimpleName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Failed to add task to queue", e);
        }
    }

    private void scheduleTask(ScheduledTask scheduledTask) {
        if (scheduledTask.isRecurring()) {
            executorService.scheduleAtFixedRate(
                    () -> executeTask(scheduledTask),
                    0,
                    scheduledTask.getInterval(),
                    scheduledTask.getTimeUnit()
            );
            logger.info("Recurring task scheduled with interval: {} {}",
                    scheduledTask.getInterval(), scheduledTask.getTimeUnit());
        } else {
            long delay = Duration.between(LocalDateTime.now(), scheduledTask.getExecutionTime()).toMillis();
            executorService.schedule(
                    () -> executeTask(scheduledTask),
                    Math.max(0, delay),
                    TimeUnit.MILLISECONDS
            );
            logger.info("One-time task scheduled for: {}", scheduledTask.getExecutionTime());
        }
    }

    private void executeTask(ScheduledTask scheduledTask) {
        try {
            scheduledTask.setState(TaskState.IN_PROGRESS);
            logger.info("Task execution started: {}", scheduledTask.getTask().getClass().getSimpleName());

            scheduledTask.getTask().run();

            scheduledTask.setState(TaskState.COMPLETED);
            logger.info("Task completed: {}", scheduledTask.getTask().getClass().getSimpleName());

            taskQueue.remove(scheduledTask);
        } catch (Exception e) {
            scheduledTask.setState(TaskState.CANCELLED);
            logger.error("Task execution failed: {}", scheduledTask.getTask().getClass().getSimpleName(), e);
        }
    }

    public void loadTasksFromFile(String filePath) {
        try {
            List<ScheduledTask> tasks = FileReaderUtility.readTasksFromFile(filePath);
            for (ScheduledTask task : tasks) {
                addTask(task);
            }
        } catch (Exception e) {
            logger.error("Failed to load tasks from file", e);
        }
    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    logger.error("Executor did not terminate");
                }
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("TaskScheduler has been shut down.");
    }
}
