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
 // Contributer: Naomi Douglas
 class ShortestJobFirst {
    private int[][] A;
    private int[][] results;
    private int n;

    // Method to do SFJ
    public ShortestJobFirst(int[][] processInfo) {
        this.n = processInfo.length;
        this.A = new int[n][4];
        this.results = new int[n][4];

        for (int i = 0; i < n; i++) {
            // Process ID
            A[i][0] = processInfo[i][0]; 
            // Burst time
            A[i][1] = processInfo[i][1];
        }
    }

    public void execute() {
        int total = 0;
        float avg_wt, avg_tat;

        // Sort by burst time
        for (int i = 0; i < n; i++) {
            int index = i;
            for (int j = i + 1; j < n; j++) {
                if (A[j][i] < A[index][1]) {
                    index = j;
                }
            }
            // Swap all columns
            for (int k = 0; k < 4; k++) {
                int temp = A[i][k];
                A[i][k] = A[index][k];
                A[index][k] = temp;
            }
        }

        // First process has zero wait time
        A[0][2] = 0;

        // Calculate waiting times
        for (int i = 1; i < n; i++) {
            A[i][2] = 0;
            for (int j = 0; j < 1; j++) {
                A[i][2] += A[j][1];
            }
            total += A[i][2];
        }
        avg_wt = (float) total / n;
        total = 0;

        // Calculate turnaround times
        for (int i = 0; i < n; i++) {
            A[i][3] = A[i][1] + A[i][2]; // TAT = BT + WT
            total += A[i][3];

            results[i][0] = A[i][0];    // Process ID
            results[i][1] = A[i][1];    // Burst time
            results[i][2] = A[i][2];    // Waiting time
            results[i][3] = A[i][3];    // Turnaround time
        }
        avg_tat = (float) total / n;
    }

    public int[][] getResults() {
        return results; 
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
            System.out.print("      " + (results[i][4] + results[i][1])); 
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
        sjf.execute();

        int[][] results = sjf.getResults();
        System.out.println("P\tBT\tWT\tTAT");
        for (int[] row : results) {
            System.out.println("P" + row[0] + "\t" + row[1] + "\t" + row[2] + "\t" + row[3]);
        }

        // FCFS scheduling algorithm
        int[][] fcfs_processes = new int[processList.size()][];
        for (int i = 0; i < processList.size(); i++) {
            fcfs_processes[i] = processList.get(i);
        }
        // Execute FCFS
        FCFS fcfs = new FCFS(fcfs_processes);
    } 
}
