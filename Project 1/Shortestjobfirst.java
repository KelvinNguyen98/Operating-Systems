// SJF
// https://www.geeksforgeeks.org/program-for-shortest-job-first-or-sjf-cpu-scheduling-set-1-non-preemptive/

import java.io.*;
import java.util.*;

public class Shortestjobfirst {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n;
        // matrix for storing process ID, burst time, waiting time, and ATT
        int [][] A = new int[100][4];
        int total = 0;
        float avg_wt, avg_tat;

        n = input.nextInt(); //input number of processes
        System.out.println("Enter Burst Time");
        for (int i = 0; i < n; i++){
            //input burst time and process id
            System.out.println("P" + (i + 1) + ": ");
            A[i][1] = input.nextInt(); //stores burst time in column 1
            A[i][0] = i + 1; //assigns process id
        }
        for (int i = 0; i < n; i++) {
            // sorting by burst time
            int index = i;
            for (int j = i + 1; j < n; j++) {
                if (A[j][1] < A[index][1]) { //finds smallest burst time
                    index = j;
                }
            }
            int temp = A[i][1];
            A[i][1] = A[index][1];
            A[index][1] = temp;  //swaps burst time of process i with the process with the shortest burst time
            temp = A[i][0];
            A[i][0] = A[index][0];
            A[index][0] = temp;//swaps process ids so they match
        }
        A[0][2] = 0; //1st process has zero wait time because there's nothing else;
        // calculates waiting times
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

        //turn arount time calculation
        for (int i = 0; i < n; i++) {
            A[i][3] = A[i][1] + A[i][2];
            // TAT = BT + WT
            total += A[i][3];
            System.out.println("P" + A[i][0] + "\t" + A[i][1] + "\t" + A[i][2] + "\t" + A[i][3]);
        }
        avg_tat = (float)total / n;
        System.out.println("Average Waiting Time= "+ avg_wt);
        System.out.println("Average Turnaround Time= " + avg_tat);

    }
}