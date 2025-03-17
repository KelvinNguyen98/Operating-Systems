// Read text file and store data in memory
// Source: w3schools

import java.io.File;                    // file class
import java.io.FileNotFoundException;   // class handles errors
import java.util.Scanner;               // scanner class to read text files

public class ReadFile {
   public static void main(String[] args) {
       try {
           File myObj = new File("Project1_processes.txt");
           Scanner myReader = new Scanner(myObj);
           System.out.println("File read successfully");
           myReader.close();

       }   catch (FileNotFoundException e) {
           System.out.println("Error opening file. Project1_processes.txt is not in the src folder.");
           e.printStackTrace();
       }
   }
}
