// Read text file and store data in memory

 import java.io.File;                    // file class
 import java.io.FileNotFoundException;   // class handles errors
 import java.util.Scanner;               // scanner class to read text files

 public class ReadFile{
    public static void main(String[] args){
        try{
            File myObj = new File("Project1_processes.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()){
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        }   catch (FileNotFoundException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
 }
