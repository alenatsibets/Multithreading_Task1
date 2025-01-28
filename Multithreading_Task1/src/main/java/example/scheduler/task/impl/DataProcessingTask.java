package example.scheduler.task.impl;


import example.scheduler.task.Task;

public class DataProcessingTask implements Task {
    private final int[] numbers;

    public DataProcessingTask(int[] numbers) {
        this.numbers = numbers;
    }

    @Override
    public void execute() {
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        System.out.println("DataProcessingTask executed: Sum of numbers = " + sum);
    }
}
