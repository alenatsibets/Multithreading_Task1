package example.scheduler;


import example.scheduler.manager.TaskScheduler;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        TaskScheduler scheduler = new TaskScheduler(3);

        String taskFilePath = Main.class.getClassLoader().getResource("tasks.txt").getPath();
        scheduler.loadTasksFromFile(taskFilePath);

        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        scheduler.shutdown();
    }
}
