/*
 *  Author: Naomi Douglas and Kelvin Nguyen
 * 
 *  This program will do First-Come, First-Served (FCFS) and Shortest Job First (SFJ)
 * 
 *  It will calculate the waiting time (WT) and turn around time (TAT) for each scheduling algorithm
 *  It will display a Gantt chart and print the waiting time (WT) for each process, 
 *  turn around time (TAT) for each process, average WT, and average TAT
 * 
 *  Sources: w3schools, geeksforgeeks
 */

 import java.io.File;                   // File class
 import java.io.FileNotFoundException;  // Class handles file errors
 import java.util.ArrayList;            // Arrays
 import java.util.List;                 // Lists
 import java.util.Scanner;              // Scanner class to read text files  
 import java.util.PriorityQueue;
 import java.util.Arrays;


 // Class to read and process the text file
 // Contributer: Naomi Douglas and Kelvin Nguyen
 class ProcessFileReader {
    private String fileName;

    // Constructor
    public ProcessFileReader(String fileName) {
        this.fileName = fileName;
    }

    // Method to read data from text file
    public List<int[]> readProcesses() {
        List<int[]> processList = new ArrayList<>();

        try {
            File file = new File(fileName);
            // Reads contents of text file
            Scanner scanner = new Scanner(file);
            
            // Ignores header of text file
            if (scanner.hasNextLine()) {
                scanner.nextLine(); 
            }

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    String[] parts = line.split("\\s+");
                    // Since our text file only has 3 columns, it'll check for 3 columns
                    if (parts.length == 3) {
                        int[] process = new int[3];
                        for (int i = 0; i < 3; i++) {
                            process[i] = Integer.parseInt(parts[i]);
                        }
                        processList.add(process);
                    }
                }
            }
            // Closes after reading contents of text file
            scanner.close();
            // Returns error if file is not found
        } catch (FileNotFoundException e) {
            System.out.printf("Error opening file: %s not found in folder%n", fileName);
            e.printStackTrace();
        }
        return processList;
    }
 }

// Class for scheduling algorithm: Shortest Job First
// Contributor: Naomi Douglas
class ShortestJobFirst {
    private int[][] results;
    private int n;

    // Constructor
    public ShortestJobFirst(int[][] processData) {
        this.n = processData.length;
        // 5 columns: PID, Arrival Time, Burst Time, Waiting Time, Turnaround Time
        this.results = new int[n][5]; 
        execute(processData);
    }

    // Method to execute SJF
    private void execute(int[][] processData) {
        int totalWT = 0, totalTAT = 0, currentTime = 0, completedCount = 0;

        // Sort by Arrival Time (to ensure processes are considered in correct order)
        Arrays.sort(processData, (p1, p2) -> Integer.compare(p1[1], p2[1]));

        // Priority Queue (Min-Heap) to always pick the shortest burst time job first
        PriorityQueue<int[]> pq = new PriorityQueue<>((p1, p2) -> Integer.compare(p1[2], p2[2]));

        int index = 0; // Tracks processes added to the queue

        while (completedCount < n) {
            // Add all processes that have arrived by `currentTime` into the PriorityQueue
            while (index < n && processData[index][1] <= currentTime) {
                pq.add(processData[index]);
                index++;
            }

            if (pq.isEmpty()) { // If no process is available, jump to the next available process
                currentTime = processData[index][1];
                continue;
            }

            // Get the shortest available process
            int[] shortestProcess = pq.poll();
            int PID = shortestProcess[0];
            int arrivalTime = shortestProcess[1];
            int burstTime = shortestProcess[2];

            int startTime = Math.max(currentTime, arrivalTime);
            int waitingTime = startTime - arrivalTime;
            int turnAroundTime = waitingTime + burstTime;

            // Store process execution results
            results[completedCount][0] = PID;
            results[completedCount][1] = burstTime;
            results[completedCount][2] = waitingTime;
            results[completedCount][3] = turnAroundTime;
            results[completedCount][4] = startTime;

            currentTime = startTime + burstTime; // Move forward in time
            totalWT += waitingTime;
            totalTAT += turnAroundTime;
            completedCount++;
        }

        // Display Gantt Chart and execution results
        GanttChart();
        displayTimes(totalWT, totalTAT);
    }

    // Method to display Gantt Chart
    private void GanttChart() {
        System.out.println("\nSJF Gantt Chart");
        for (int i = 0; i < n; i++) {
            System.out.printf("|  P%d  ", results[i][0]);
        }
        System.out.println("|");

        System.out.print(results[0][4]); // Start time of first process
        for (int i = 0; i < n; i++) {
            System.out.printf("      %d", results[i][4] + results[i][1]); 
        }
        System.out.println();
    }

    // Method to display Waiting Time (WT), Turnaround Time (TAT), and Averages
    private void displayTimes(int totalWT, int totalTAT) {
        System.out.println("\nWaiting Time (WT) and Turnaround Time (TAT) for each process:");
        for (int i = 0; i < n; i++) {
            System.out.printf("Process P%d: WT = %d, TAT = %d%n", results[i][0], results[i][2], results[i][3]);
        }

        double averageWT = (double) totalWT / n;
        double averageTAT = (double) totalTAT / n;
        System.out.printf("%nAverage Waiting Time (WT) = %.2f", averageWT);
        System.out.printf("%nAverage Turnaround Time (TAT) = %.2f%n%n", averageTAT);
    }
}


 // Class for scheduling algorithm: First-Come, First-Served (FCFS)
 // Contributer: Kelvin Nguyen
 class FCFS {
    private int[][] results; 
    private int n;

    // Constructor
    public FCFS(int[][] processData) {
        this.n = processData.length;
        // 5 columns: PID, burst time, waiting time, turn around time, start time
        this.results = new int[n][5]; 
        execute(processData);
    }

    // Method to execute FCFS
    private void execute(int[][] processData) {
        int totalWT = 0, totalTAT = 0, currentTime = 0;

        // Sort by arrival time
        java.util.Arrays.sort(processData, java.util.Comparator.comparingInt(p -> p[1]));

        for (int i = 0; i < n; i++) {
            int PID = processData[i][0];
            int arrivalTime = processData[i][1];
            int burstTime = processData[i][2];

            int startTime = Math.max(currentTime, arrivalTime);
            int waitingTime = startTime - arrivalTime;
            int turnAroundTime = waitingTime + burstTime; 
 
            results[i][0] = PID;
            results[i][1] = burstTime;
            results[i][2] = waitingTime;
            results[i][3] = turnAroundTime;
            results[i][4] = startTime;

            currentTime = startTime + burstTime;
            totalWT += waitingTime;
            totalTAT += turnAroundTime;
        }

        // Display Gantt chart
        GanttChart();
        // Display Waiting Time (WT), Turn Around Time (TAT), average waiting time, and average turn around time
        displayTimes(totalWT, totalTAT);
    }

    // Method to display Gantt chart
    private void GanttChart() {
        System.out.println("\nFCFS Gantt Chart");
        for (int i = 0; i < n; i++) {
            System.out.printf("|  P%d  ", results[i][0]);
        }
        System.out.println("|");

        // Start time of first process
        System.out.print(results[0][4]);
        // End time of each process
        for (int i = 0; i < n; i++) {
            System.out.printf("      %d", results[i][4] + results[i][1]); 
        }
        System.out.println();
    }

    // Method to return Waiting Time (WT), Turn Around Time (TAT), average waiting time, and average turn around time
    private void displayTimes(int totalWT, int totalTAT) {
        System.out.println("\nWaiting Time (WT) and Turn Around Time (TAT) for each process:");
        for (int i = 0; i < n; i++) {
            System.out.printf("Process P%d: WT = %d, TAT = %d%n", results[i][0], results[i][2], results[i][3]);
        }

        // Calculate and display average Waiting Time (WT) and Average Turn Around (TAT)
        double averageWT = (double) totalWT / n;
        double averageTAT = (double) totalTAT / n;
        System.out.printf("%nAverage Waiting Time (WT) = %.2f", averageWT);
        System.out.printf("%nAverage Turn Around Time (AT) = %.2f%n%n", averageTAT);
    }
 }

 // Main method
 // Contributer: Naomi Douglas and Kelvin Nguyen
 public class Main {
    public static void main(String[] args) {
        // Path to text file
        ProcessFileReader reader = new ProcessFileReader("Project1_processes.txt");
        List <int[]> processList = reader.readProcesses();

        // If text file is empty, return message
        if (processList.isEmpty()) {
            System.out.println("No data to be processed."); 
            return;
        }

        // Convert list to array
        int[][] processes = new int[processList.size()][];
        for (int i = 0; i < processList.size(); i++) {
            processes[i] = processList.get(i);
        }

        ShortestJobFirst sjf = new ShortestJobFirst(processes);

        // FCFS scheduling algorithm
        int[][] fcfs_processes = new int[processList.size()][];
        for (int i = 0; i < processList.size(); i++) {
            fcfs_processes[i] = processList.get(i);
        }
        // Execute FCFS
        FCFS fcfs = new FCFS(fcfs_processes);
    } 
}
