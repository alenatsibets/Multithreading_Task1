package example.scheduler.task;

import example.scheduler.state.TaskState;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class ScheduledTask {
    private final Task task;
    private LocalDateTime executionTime;
    private final long interval;
    private final TimeUnit timeUnit;
    private TaskState state;

    public ScheduledTask(Task task, LocalDateTime executionTime) {
        this.task = task;
        this.executionTime = executionTime;
        this.interval = -1; // Indicates no interval
        this.timeUnit = null;
        this.state = TaskState.PENDING;
    }

    public ScheduledTask(Task task, long interval, TimeUnit timeUnit) {
        this.task = task;
        this.executionTime = null;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.state = TaskState.PENDING;
    }

    public Task getTask() {
        return task;
    }

    public LocalDateTime getExecutionTime() {
        return executionTime;
    }

    public long getInterval() {
        return interval;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public TaskState getState() {
        return state;
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public boolean isRecurring() {
        return interval > 0;
    }

    public boolean isCompleted() {
        return state == TaskState.COMPLETED;
    }

    public boolean isCancelled() {
        return state == TaskState.CANCELLED;
    }
}