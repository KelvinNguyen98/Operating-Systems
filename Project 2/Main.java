/*
 * Author: Naomi Douglas and Kelvin Nguyen
 * 
 * This program will simulate real-time process execution using threads
 * and how it handles synchronization
 * 
 * The synchronization problem used: Producer-Consumer
 * It will use mutexes and semaphores
 * 
 * Sources: geeksforgeeks
 */


 import java.util.*;            // Arrays, Lists, etc. 
 import java.util.concurrent.*; // Semaphore and and ReentrantLock
 import java.io.*;              // File, Exception, etc. 
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

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

            // Ignores the header of text file
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

 // Taken from PDF of project
 // Thread to simulate process
 class ProcessThread extends Thread {
    int pid, burstTime;
    public ProcessThread(int pid, int burstTime) {
        this.pid = pid;
        this.burstTime = burstTime;
    }
    public void run() {
        System.out.println("Process " + pid + " started.");
        try {
            Thread.sleep(burstTime * 1000);
        } catch (InterruptedException e) {}
        System.out.println("Process " + pid + " finished.");
    }
 }

 // Buffer for producer-consumer
 // Contributer: Kelvin Nguyen
 class buffer {
    // Variables that cannot be changed and are private to the other methods
    // Linked list
    private final Queue<Integer> queue = new LinkedList<>();
    // Capacity of buffer
    private final int capacity;
    
    //Two semaphores for buffer

    //Number of buffer slots
    //Incremented each time producer places new data item into buffer
    //Decremented each time consumer removes item from buffer
    private final Semaphore f;
    // Number of empty slots
    private final Semaphore e;
    // Lock (synchronization barrier)
    private final Lock mutex = new ReentrantLock();

    // Buffer method
    public buffer(int capacity) {
        this.capacity = capacity;
        this.f = new Semaphore(0);
        this.e = new Semaphore(capacity);
    }

    // Producer method for thread activity
    public void produce(int item) throws InterruptedException {
        System.out.println("[Producer] Waiting for empty slot...");
        empty.acquire();
        mutex.lock();
        try {
            queue.add(item);
            System.out.println("[Producer] Produced item: " + item);
        } finally {
            mutex.unlock();
            full.release();
        }
    }

    // Consumer method for thread activity
    public int consume(int consumerID) throws InterruptedException {
        System.out.println("[Consumer " + consumerID + "] Waiting for item...");
        full.acquire();
        mutex.lock();
        int item;
        try {
            item = queue.remove();
            System.out.println("[Consumer " + consumerID + "] Consumed item: " + item);
        } finally {
            mutex.unlocked();
            empty.release();
        }
        return item;
    }
 }

 // Main Method
 // Contributer: Noami Douglas and Kelvin Nguyen
 public class Main {
    public static void main(String[] args) {
        // Path to text file
        ProcessFileReader reader = new ProcessFileReader("Project2_processes.txt");
        List <int[]> processList = reader.readProcesses();

        // If text file is empty, return message
        if (processList.isEmpty()) {
            System.out.println("No data to be processed.");
            return;
        }
    }


 }
