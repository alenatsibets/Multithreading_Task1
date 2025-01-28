package example.scheduler.task.impl;

import example.scheduler.task.Task;

public class PrintTask implements Task {
    private final String message;

    public PrintTask(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println("Executing PrintTask: " + message);
    }
}
