package example.scheduler.manager.reader;


import example.scheduler.task.impl.*;
import example.scheduler.task.ScheduledTask;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileReaderUtility {
    public static List<ScheduledTask> readTasksFromFile(String filePath) {
        List<ScheduledTask> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                ScheduledTask task = parseTask(parts);
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading tasks from file: " + e.getMessage());
        }
        return tasks;
    }

    private static ScheduledTask parseTask(String[] parts) {
        try {
            String taskType = parts[0].trim();

            switch (taskType) {
                case "PrintTask": {
                    String message = parts[1].trim();
                    LocalDateTime executionTime = LocalDateTime.parse(parts[2].trim());
                    return new ScheduledTask(new PrintTask(message), executionTime);
                }
                case "FileWriteTask": {
                    String filePath = parts[1].trim();
                    String message = parts[2].trim();
                    long interval = Long.parseLong(parts[3].trim());
                    TimeUnit timeUnit = TimeUnit.valueOf(parts[4].trim());
                    return new ScheduledTask(new FileWriteTask(filePath, message), interval, timeUnit);
                }
                case "DataProcessingTask": {
                    int[] numbers = new int[parts.length - 2];
                    for (int i = 1; i < parts.length - 1; i++) {
                        numbers[i - 1] = Integer.parseInt(parts[i].trim());
                    }
                    LocalDateTime executionTime = LocalDateTime.parse(parts[parts.length - 1].trim());
                    return new ScheduledTask(new DataProcessingTask(numbers), executionTime);
                }
                default:
                    System.err.println("Unknown task type: " + taskType);
                    return null;
            }
        } catch (DateTimeParseException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing task: " + e.getMessage());
            return null;
        }
    }
}
