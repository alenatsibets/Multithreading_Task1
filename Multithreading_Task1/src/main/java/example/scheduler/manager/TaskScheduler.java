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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskScheduler {
    private static final Logger logger = LogManager.getLogger(TaskScheduler.class);
    private final ScheduledExecutorService executorService;
    private final Lock fileLock;
    private final List<ScheduledTask> taskList;

    public TaskScheduler(int poolSize) {
        this.executorService = Executors.newScheduledThreadPool(poolSize);
        this.fileLock = new ReentrantLock();
        this.taskList = new CopyOnWriteArrayList<>();
    }

    public void addTask(ScheduledTask scheduledTask) {
        taskList.add(scheduledTask);
        if (scheduledTask.isRecurring()) {
            executorService.scheduleAtFixedRate(
                    () -> executeTask(scheduledTask),
                    0,
                    scheduledTask.getInterval(),
                    scheduledTask.getTimeUnit()
            );
            logger.info("Recurring task added with interval: {} {}",
                    scheduledTask.getInterval(),
                    scheduledTask.getTimeUnit());
        } else {
            long delay = Duration.between(LocalDateTime.now(), scheduledTask.getExecutionTime()).toMillis();
            executorService.schedule(
                    () -> executeTask(scheduledTask),
                    delay,
                    TimeUnit.MILLISECONDS
            );
            logger.info("One-time task scheduled for: {}", scheduledTask.getExecutionTime());
        }
    }

    private void executeTask(ScheduledTask scheduledTask) {
        logger.info("Task execution started: {}", scheduledTask.getTask().getClass().getSimpleName());

        scheduledTask.setState(TaskState.IN_PROGRESS);
        logger.info("Task state changed: {} -> {}", TaskState.PENDING, TaskState.IN_PROGRESS);

        scheduledTask.getTask().execute();

        if (!scheduledTask.isRecurring()) {
            scheduledTask.setState(TaskState.COMPLETED);
            logger.info("Task state changed: {} -> {}", TaskState.IN_PROGRESS, TaskState.COMPLETED);

            if (scheduledTask.isCompleted()) {
                taskList.remove(scheduledTask);
                logger.info("Task completed and removed from the list: {}", scheduledTask.getTask().getClass().getSimpleName());
            }
        } else {
            logger.info("Recurring task executed, next execution in {} {}",
                    scheduledTask.getInterval(),
                    scheduledTask.getTimeUnit());
        }

        if (scheduledTask.isCompleted()) {
            taskList.remove(scheduledTask);
            logger.info("Task completed and removed from the list: {}", scheduledTask.getTask().getClass().getSimpleName());
        }
    }

    public void loadTasksFromFile(String filePath) {
        fileLock.lock();
        try {
            List<ScheduledTask> tasks = FileReaderUtility.readTasksFromFile(filePath);
            for (ScheduledTask task : tasks) {
                addTask(task);
                System.out.println(task.getState());
            }
        } finally {
            fileLock.unlock();
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