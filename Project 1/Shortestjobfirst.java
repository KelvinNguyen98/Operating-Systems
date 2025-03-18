// SJF
// Sources: https://www.geeksforgeeks.org/program-for-shortest-job-first-or-sjf-cpu-scheduling-set-1-non-preemptive/

import java.util.*;

class ShortestJobFirst {
    private int[][] A;
    private int n;

    public ShortestJobFirst(int[][] processInfo) {
        this.n = processInfo.length;
        this.A = new int[n][4];
        
        // Copy input data
        for (int i = 0; i < n; i++) {
            A[i][0] = processInfo[i][0]; // Process ID
            A[i][1] = processInfo[i][1]; // Burst Time
        }
    }

    public void execute() {
        int total = 0;
        float avg_wt, avg_tat;

        // Sorting by burst time
        for (int i = 0; i < n; i++) {
            int index = i;
            for (int j = i + 1; j < n; j++) {
                if (A[j][1] < A[index][1]) { // Finds smallest burst time
                    index = j;
                }
            }
            int temp = A[i][1];
            A[i][1] = A[index][1];
            A[index][1] = temp; // Swap burst time
            temp = A[i][0];
            A[i][0] = A[index][0];
            A[index][0] = temp; // Swap process IDs
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

        System.out.println("P\tBT\tWT\tTAT");

        // Turnaround time calculation
        for (int i = 0; i < n; i++) {
            A[i][3] = A[i][1] + A[i][2]; // TAT = BT + WT
            total += A[i][3];
            System.out.println("P" + A[i][0] + "\t" + A[i][1] + "\t" + A[i][2] + "\t" + A[i][3]);
        }
        avg_tat = (float) total / n;
        System.out.println("Average Waiting Time= " + avg_wt);
        System.out.println("Average Turnaround Time= " + avg_tat);
    }
}
