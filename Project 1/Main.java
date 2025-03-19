/*
 *  This program will do First-Come, First-Served (FCFS) and Shortest Job First (SFJ)
 * 
 *  It calculate the waiting time (WT) and turn around time (TAT) for each scheduling algorithms
 * 
 *  It will display a Gantt chart and print the waiting time (WT) for each process, 
 *  turn around time (TAT) for each process, average WT, and average TAT
 * 
 *  Sources: w3schools, geeksforgeeks
 */

 import java.io.File;
 import java.io.FileNotFoundException;
 import java.util.Scanner;
 import java.util.ArrayList;
 import java.util.List;
 
 class ProcessFileReader {
     private String fileName;
 
     public ProcessFileReader(String fileName) {
         this.fileName = fileName;
     }
 
     public List<int[]> readProcesses() {
         List<int[]> processList = new ArrayList<>();
 
         try {
             File file = new File(fileName);
             Scanner scanner = new Scanner(file);
 
             if (scanner.hasNextLine()) {
                 scanner.nextLine(); // Ignore header
             }
 
             while (scanner.hasNextLine()) {
                 String line = scanner.nextLine().trim();
                 if (!line.isEmpty()) {
                     String[] parts = line.split("\\s+");
                     if (parts.length == 3) { // Ensure there are exactly 3 columns
                         int[] process = new int[3];
                         for (int i = 0; i < 3; i++) {
                             process[i] = Integer.parseInt(parts[i]);
                         }
                         processList.add(process);
                     }
                 }
             }
 
             scanner.close();
         } catch (FileNotFoundException e) {
             System.out.println("Error opening file: " + fileName + " not found.");
             e.printStackTrace();
         }
 
         return processList;
     }
 }
 
 class ShortestJobFirst {
     private int[][] A;
     private int[][] results;
     private int n;
 
     public ShortestJobFirst(int[][] processInfo) {
         this.n = processInfo.length;
         this.A = new int[n][4];
         this.results = new int[n][4];
 
         for (int i = 0; i < n; i++) {
             A[i][0] = processInfo[i][0]; // Process ID
             A[i][1] = processInfo[i][1]; // Burst Time
         }
     }
 
     public void execute() {
         int total = 0;
         float avg_wt, avg_tat;
 
         // Sort by burst time
         for (int i = 0; i < n; i++) {
             int index = i;
             for (int j = i + 1; j < n; j++) {
                 if (A[j][1] < A[index][1]) {
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
 
         A[0][2] = 0; // First process has zero wait time
 
         // Calculate waiting times
         for (int i = 1; i < n; i++) {
             A[i][2] = 0;
             for (int j = 0; j < i; j++) {
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
 
             results[i][0] = A[i][0]; // Process ID
             results[i][1] = A[i][1]; // Burst Time
             results[i][2] = A[i][2]; // Waiting Time
             results[i][3] = A[i][3]; // Turnaround Time
         }
         avg_tat = (float) total / n;
     }
 
     public int[][] getResults() {
         return results;
     }
 }
 
 public class Main {
     public static void main(String[] args) {
         ProcessFileReader reader = new ProcessFileReader("Project1_processes.txt"); // Replace with your file path
         List<int[]> processList = reader.readProcesses();
 
         if (processList.isEmpty()) {
             System.out.println("No valid process data found.");
             return;
         }
 
         // Convert List<int[]> to int[][]
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
