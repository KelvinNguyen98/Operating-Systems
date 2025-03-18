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

import java.io.File;                    // File class
import java.io.FileNotFoundException;   // Class handles error
import java.util.Scanner;               // Scanner class to read text files

public class Main {
    // This function reads data from the Project1_processes file
    // and stores the data in memory
    public static void ReadFile(String[] args) {
        try {
            File myObj = new File("Project1_processes.txt");
            // Read data from the text file
            Scanner myReader = new Scanner(myObj);
            // Close scanner to stop reading from the text file
            myReader.close();

            // If an error occurs, the file will not be read and will display an error message
        }   catch (FileNotFoundException e) {
            System.out.println("Error opening file. Project1_processes.txt is not in the src folder.");
            e.printStackTrace();
        }
    }

    // Main function
    public static void Main(String[] args) {
        ;
    }
}
