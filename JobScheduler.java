import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JobScheduler {
    private static class Job {
        int jobId;
        int processingTime;

        public Job(int jobId, int processingTime) {
            this.jobId = jobId;
            this.processingTime = processingTime;
        }
    }

    public static void main(String[] args) {
        List<Job> jobList = new ArrayList<>();

            // Read jobs from file
        try {
            File file = new File("task1-input.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split(" ");
                int jobId = Integer.parseInt(line[0]);
                int processingTime = Integer.parseInt(line[1]);
                jobList.add(new Job(jobId, processingTime));
            }
            scanner.close();
        }

        catch (FileNotFoundException e) {
                System.err.println("File not found. Please ensure 'task1-input.txt' exists in the directory.");
                return;
        }

        // Create a priority queue with a capacity equal to the number of jobs
        int n = jobList.size();
        IndexMinPQ<Integer> minPQ = new IndexMinPQ<>(n);

        // Insert jobs into the priority queue based on processing time
        for (int i = 0; i < n; i++) {
                minPQ.insert(i, jobList.get(i).processingTime);
        }

        // To store the execution order
        List<Integer> executionOrder = new ArrayList<>();
        int currentTime = 0;
        int totalCompletionTime = 0;

        // Schedule jobs in order of shortest processing time
        while (!minPQ.isEmpty()) {
            // Get the index of the job with the shortest processing time
            int index = minPQ.delMin();
            Job job = jobList.get(index);

            // Update the current time with the job's processing time
            currentTime += job.processingTime;
            // Add to total completion time
            totalCompletionTime += currentTime;
            // Record the job's execution order by ID
            executionOrder.add(job.jobId);
        }

        // Calculate the average completion time
        double averageCompletionTime = (double) totalCompletionTime / n;

        // Output the results
        System.out.println("Execution order: " + executionOrder);
        System.out.printf("Average completion time: %.2f\n", averageCompletionTime);
        }
    }

