import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class DynamicJobScheduler {
    private static class Job implements Comparable<Job> {
        int jobId;
        int processingTime;
        int arrivalTime;

        public Job(int jobId, int processingTime, int arrivalTime) {
            this.jobId = jobId;
            this.processingTime = processingTime;
            this.arrivalTime = arrivalTime;
        }

        @Override
        public int compareTo(Job other) {
            return Integer.compare(this.processingTime, other.processingTime);
        }
    }

    public static void main(String[] args) {
        List<Job> allJobs = new ArrayList<>();

        // Read jobs from file
        try {
            File file = new File("task3-input.txt");
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split("[,\\s]+"); // Split by comma or space
                int jobId = Integer.parseInt(parts[0].trim());
                int processingTime = Integer.parseInt(parts[1].trim());
                int arrivalTime = Integer.parseInt(parts[2].trim());
                allJobs.add(new Job(jobId, processingTime, arrivalTime));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found. Please ensure 'task3-input.txt' exists in the directory.");
            return;
        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid input format in 'task3-input.txt'. Ensure all lines have three integer values.");
            return;
        }

        // Sort jobs by arrival time
        allJobs.sort(Comparator.comparingInt(job -> job.arrivalTime));

        // Priority queue for jobs based on processing time (min-heap)
        PriorityQueue<Job> jobQueue = new PriorityQueue<>();
        int currentTime = 0;
        int totalCompletionTime = 0;
        List<Integer> executionOrder = new ArrayList<>();
        int jobIndex = 0;

        while (jobIndex < allJobs.size() || !jobQueue.isEmpty()) {
            // Add jobs that have arrived by the current time to the queue
            while (jobIndex < allJobs.size() && allJobs.get(jobIndex).arrivalTime <= currentTime) {
                jobQueue.offer(allJobs.get(jobIndex));
                jobIndex++;
            }

            // Process the job with the shortest processing time
            if (!jobQueue.isEmpty()) {
                Job currentJob = jobQueue.poll();
                currentTime += currentJob.processingTime;
                totalCompletionTime += currentTime; // add current time to total completion time
                executionOrder.add(currentJob.jobId);
            } else {
                // If no jobs are available, increment time to the next job's arrival time
                if (jobIndex < allJobs.size()) {
                    currentTime = allJobs.get(jobIndex).arrivalTime;
                }
            }
        }

        // Calculate the average completion time
        double averageCompletionTime = (double) totalCompletionTime / allJobs.size();

        // Output results
        System.out.println("Execution order: " + executionOrder);
        System.out.printf("Average completion time: %.2f\n", averageCompletionTime);
    }
}

