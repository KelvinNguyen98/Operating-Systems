// SJF
// Sources: https://www.geeksforgeeks.org/program-for-shortest-job-first-or-sjf-cpu-scheduling-set-1-non-preemptive/

import java.util.*;

class ShortestJobFirst {
    private int[][] A;
    private int[][] results; // Stores final process info
    private int n;

    public ShortestJobFirst(int[][] processInfo) {
        this.n = processInfo.length;
        this.A = new int[n][4];
        this.results = new int[n][4]; // matrix for storing process ID, burst time, waiting time, and ATT
        
        // Copy input data
        for (int i = 0; i < n; i++) {
            A[i][0] = processInfo[i][0]; // Process ID
            A[i][1] = processInfo[i][1]; // Burst Time
        }
    }

    public void execute() {
        int total = 0;
        float avg_wt, avg_tat;

        // sorting by burst time
        for (int i = 0; i < n; i++) {
            int index = i;
            for (int j = i + 1; j < n; j++) {
                if (A[j][1] < A[index][1]) { // Finds smallest burst time
                    index = j;
                }
            }
            int temp = A[i][1];
            A[i][1] = A[index][1];
            A[index][1] = temp; // swap burst time
            temp = A[i][0];
            A[i][0] = A[index][0];
            A[index][0] = temp; // swap process IDs
        }

        A[0][2] = 0; // first process has zero wait time
        
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

        // Turnaround time calculation
        for (int i = 0; i < n; i++) {
            A[i][3] = A[i][1] + A[i][2]; // TAT = BT + WT
            total += A[i][3];

            // Store results in the results array
            results[i][0] = A[i][0]; // Process ID
            results[i][1] = A[i][1]; // Burst Time
            results[i][2] = A[i][2]; // Waiting Time
            results[i][3] = A[i][3]; // Turnaround Time
        }
        avg_tat = (float) total / n;
    }

    // getter method to retrieve results
    public int[][] getResults() {
        return results;
    }
}
