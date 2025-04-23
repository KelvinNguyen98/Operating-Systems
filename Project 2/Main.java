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

 import java.io.*;      // File, Exception, etc. 
 import java.util.*;    // Arrays, Lists, etc.
 import java.util.concurrent.*;   // Semaphores
 import java.util.concurrent.locks.*;  // ReentrantLock

 // Class to read and process the text file
 // Contributor: Naomi Douglas and Kelvin Nguyen
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
 // Contributor: Kelvin Nguyen
 class Buffer {
    // Queue
    private final Queue<Integer> queue = new LinkedList<>();
    // Capacity of buffer
    private final int capacity;
    
    //Two semaphores for buffer:
    //Number of buffer slots
    //Incremented each time producer places new data item into buffer
    //Decremented each time consumer removes item from buffer
    private final Semaphore f;
    // Number of empty slots
    private final Semaphore e;
    // Lock (synchronization barrier)
    private final Lock mutex = new ReentrantLock();

    // Buffer method
    public Buffer(int capacity) {
        this.capacity = capacity;
        // No items in buffer
        this.f = new Semaphore(0);
        // Empty slots in buffer
        this.e = new Semaphore(capacity);
    }

    // Producer method for thread activity
    public void produce(int item) throws InterruptedException {
        System.out.println("[Producer] Waiting for empty slot...");
        // Producer acquires permit to put item into buffer
        e.acquire();
        // Locks so that only one thread can access it
        mutex.lock();
        try {
            // Producer adds item to buffer 
            queue.add(item);
            System.out.printf("[Producer] Produced item: %d%n", item);
        } finally {
            // Unlocks for next thread to access
            mutex.unlock();
            // Releases permit
            f.release();
        }
    }

    // Consumer method for thread activity
    public int consume(int consumerID) throws InterruptedException {
        System.out.printf("[Consumer %d] Waiting for item...%n", consumerID);
        // Consumer acquires permit to remove item from buffer
        f.acquire();
        // Locks so that only one thread can access it
        mutex.lock();
        // Variable to hold items
        int item;
        try {
            // Consumer removes item from buffer
            item = queue.remove();
            System.out.printf("[Consumer %d] Consumed item: %d%n", consumerID, item);
        } finally {
            // Unlocks for next thread to access
            mutex.unlock();
            // Releases permit
            e.release();
        }
        return item;
    }
 }

 // Producer class
 // Contributor: Naomi Douglas
 class Producer extends Thread {
    // Buffer for producer
    private final Buffer buffer;
    // Number of items
    private final int numItems;

    // Producer method 
    public Producer(Buffer buffer, int numItems) {
        this.buffer = buffer;
        this.numItems = numItems;
    }

    // Method to simulate production time
    public void run() {
        for (int i = 1; i <= numItems; i++) {
            try {
                // Thread waits
                Thread.sleep(1000);
                // Shows thread activity (producer)
                buffer.produce(i);
            } catch (InterruptedException e) {}
        }
        System.out.println("[Producer] Finished producing.");
    }
 }

 // Consumer class
 // Contributor: Naomi Douglas
 class Consumer extends Thread {
    // Buffer for consumer
    private final Buffer buffer; 
    // ID
    private final int id;

    // Consumer method
    public Consumer(Buffer buffer, int id) {
        this.buffer = buffer;
        this.id = id;
    }
    
    // Method to simulate consumption time
    public void run() {
        try {
            while (true) { 
                // Thread waits
                Thread.sleep(1500);
                // Shows thread activity (consumer)
                buffer.consume(id);
            }
        } catch (InterruptedException e) {}
    }
 }

 // Main Method
 // Contributor: Noami Douglas and Kelvin Nguyen
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

        // Track real time start
        long startTime = System.currentTimeMillis();
        List<Thread> threads = new ArrayList<>();

        // Process thread at arrival time
        for (int[] proc : processList) {
            int pid = proc[0];
            int arrival = proc[1];
            int burst = proc[2];

            try {
                long delay = (arrival * 1000) - (System.currentTimeMillis() - startTime);
                // Waits until arrival time
                if (delay > 0) Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Create process thread
            ProcessThread pt = new ProcessThread(pid, burst);
            threads.add(pt);
            // Start process
            pt.start();
        }

        // Wait for threads to finish
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n--- Producer-Consumer Simulation --- \n");

        // Buffer size is 5
        int bufferSize = 5;
        Buffer buffer = new Buffer(bufferSize);
        // 5 items to produce 
        Producer producer = new Producer(buffer, 5);
        producer.start();

        int numConsumers = 5;
        for (int i = 0; i < numConsumers; i++) {
            // Start each consumer
            new Consumer(buffer, i + 1).start();
        }

        try {
            // Wait for producer to finish
            producer.join();
            // Allow consumers to process 
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Closes consumer threads
        System.exit(0);
    }
 }
