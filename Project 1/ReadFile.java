/* 
   This program reads data from the Project1_processes file and stores the data in memory
   Source: w3schools
*/

import java.io.File;                    // file class
import java.io.FileNotFoundException;   // class handles errors
import java.util.Scanner;               // scanner class to read text files

public class ReadFile {
   public static void main(String[] args) {
       try {
           File myObj = new File("Project1_processes.txt");
           // Read data from the text file
           Scanner myReader = new Scanner(myObj);
           // After reading, it will display that the file was read successfully
           System.out.println("File read successfully");
           // Close scanner to stop reading from the text file
           myReader.close();

           // If an error occurs, the file will not be read and will display an error message
       }   catch (FileNotFoundException e) {
           System.out.println("Error opening file. Project1_processes.txt is not in the src folder.");
           e.printStackTrace();
       }
   }
}
