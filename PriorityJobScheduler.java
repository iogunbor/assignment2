import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PriorityJobScheduler {
    private static class Job {
        int jobId;
        int processingTime;
        int priorityClass;

        public Job(int jobId, int processingTime, int priorityClass) {
            this.jobId = jobId;
            this.processingTime = processingTime;
            this.priorityClass = priorityClass;
        }
    }

    public static void main(String[] args) {
        List<Job> jobList = new ArrayList<>();

        // Read jobs from file
        try {
            File file = new File("task2-input.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                int jobId = Integer.parseInt(line[0]);
                int processingTime = Integer.parseInt(line[1]);
                int priorityClass = Integer.parseInt(line[2]);
                jobList.add(new Job(jobId, processingTime, priorityClass));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found. Please ensure 'task1-input.txt' exists in the directory.");
            return;
        }

        // Sort jobs first by priorityClass (ascending) and then by processingTime (ascending)
        jobList.sort((job1, job2) -> {
            if (job1.priorityClass != job2.priorityClass) {
                return Integer.compare(job1.priorityClass, job2.priorityClass); // Higher priority class first
            }
            return Integer.compare(job1.processingTime, job2.processingTime); // SPT within same priority class
        });

        // To store the execution order and calculate completion time
        List<Integer> executionOrder = new ArrayList<>();
        int currentTime = 0;
        int totalCompletionTime = 0;

        // Schedule jobs according to sorted order
        for (Job job : jobList) {
            // Update the current time with the job's processing time
            currentTime += job.processingTime;
            // Add to total completion time
            totalCompletionTime += currentTime;
            // Record the job's execution order by ID
            executionOrder.add(job.jobId);
        }

        // Calculate the average completion time
        double averageCompletionTime = (double) totalCompletionTime / jobList.size();

        // Output the results
        System.out.println("Execution order: " + executionOrder);
        System.out.printf("Average completion time: %.2f\n", averageCompletionTime);
    }
}

