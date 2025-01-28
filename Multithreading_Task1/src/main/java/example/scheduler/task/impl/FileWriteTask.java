package example.scheduler.task.impl;


import example.scheduler.task.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class FileWriteTask implements Task {
    private final String filePath;
    private final String message;

    public FileWriteTask(String filePath, String message) {
        this.filePath = filePath;
        this.message = message;
    }

    @Override
    public void execute() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            String logMessage = LocalDateTime.now() + " - " + message;
            writer.write(logMessage);
            writer.newLine();
            System.out.println("FileWriteTask executed: " + logMessage);
        } catch (IOException e) {
            System.err.println("Failed to write to file: " + e.getMessage());
        }
    }
}
