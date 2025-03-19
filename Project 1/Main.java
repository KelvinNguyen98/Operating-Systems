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
 import java.util.ArrayList;            // Scanner class to read text files
 import java.util.List;                 // Arrays
 import java.util.Scanner;              // Lists

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
    } 
}
