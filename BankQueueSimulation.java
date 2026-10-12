//Group Members
//153227	Husna Musa
//190503	Tivon Mabururu 
//193010    Kuria James
//192570	Chebulobi Derrick Barasa 
//173544	Nicodemus Edwardsimport java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Customer {
    int id;
    double interArrivalTime;
    double arrivalTime;
    double serviceStartTime;
    double serviceTime;
    double serviceEndTime;
    double waitingTime;
    double timeInSystem;
    double serverIdleTime;

    public Customer(int id, double interArrivalTime, double arrivalTime, double serviceTime) {
        this.id = id;
        this.interArrivalTime = interArrivalTime;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }
}

public class BankQueueSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==================================================");
        System.out.println("         BANK QUEUE SIMULATION - INPUT SCREEN      ");
        System.out.println("==================================================");
        System.out.print("Enter number of customers to simulate (default 100): ");
        String input = scanner.nextLine();
        int numCustomers = input.isEmpty() ? 100 : Integer.parseInt(input);

        double minIAT = 1.0; 
        double maxIAT = 5.0; 
        double minST = 1.0;  
        double maxST = 6.0;  

        System.out.println("\nSimulation Parameters Loaded Successfully:");
        System.out.println(" - Inter-arrival Time: Uniform (" + minIAT + ", " + maxIAT + ") minutes");
        System.out.println(" - Service Time      : Uniform (" + minST + ", " + maxST + ") minutes");
        System.out.println(" - Total Customers   : " + numCustomers);
        System.out.println("==================================================");
        System.out.println("Press ENTER to run the simulation...");
        scanner.nextLine();

        
        List<Customer> customers = new ArrayList<>();
        Random random = new Random();

        double clock = 0.0;
        double totalIdleTime = 0.0;
        double totalWaitingTime = 0.0;
        double totalTimeInSystem = 0.0;

        for (int i = 1; i <= numCustomers; i++) {
            double iat = 0.0;
            if (i > 1) {
                
                iat = minIAT + (maxIAT - minIAT) * random.nextDouble();
            }
            clock += iat;
            double arrivalTime = clock;

            
            double st = minST + (maxST - minST) * random.nextDouble();

            double serviceStartTime;
            double idleTime = 0.0;

            if (i == 1) {
                serviceStartTime = arrivalTime;
                idleTime = arrivalTime; 
            } else {
                Customer prev = customers.get(i - 2);
                if (arrivalTime >= prev.serviceEndTime) {
                    serviceStartTime = arrivalTime;
                    idleTime = arrivalTime - prev.serviceEndTime;
                } else {
                    serviceStartTime = prev.serviceEndTime;
                    idleTime = 0.0;
                }
            }

            totalIdleTime += idleTime;
            double serviceEndTime = serviceStartTime + st;
            double waitingTime = serviceStartTime - arrivalTime;
            double timeInSystem = serviceEndTime - arrivalTime;

            totalWaitingTime += waitingTime;
            totalTimeInSystem += timeInSystem;

            Customer c = new Customer(i, iat, arrivalTime, st);
            c.serviceStartTime = serviceStartTime;
            c.serviceEndTime = serviceEndTime;
            c.waitingTime = waitingTime;
            c.timeInSystem = timeInSystem;
            c.serverIdleTime = idleTime;

            customers.add(c);
        }

        
        System.out.println("\n=====================================================================================");
        System.out.println("                         BANK QUEUE SIMULATION - OUTPUT TABLE                        ");
        System.out.println("=====================================================================================");
        System.out.printf("%-10s %-12s %-14s %-14s %-14s %-14s %-14s%n", 
                "Customer", "IAT (min)", "Arrival Time", "Service Time", "Start Time", "End Time", "Wait Time");
        System.out.println("-------------------------------------------------------------------------------------");

        
        for (int i = 0; i < Math.min(15, customers.size()); i++) {
            Customer c = customers.get(i);
            System.out.printf("%-10d %-12.2f %-14.2f %-14.2f %-14.2f %-14.2f %-14.2f%n",
                    c.id, c.interArrivalTime, c.arrivalTime, c.serviceTime, c.serviceStartTime, c.serviceEndTime, c.waitingTime);
        }
        if (customers.size() > 15) {
            System.out.println("... [Middle rows omitted for display clarity] ...");
            for (int i = customers.size() - 5; i < customers.size(); i++) {
                Customer c = customers.get(i);
                System.out.printf("%-10d %-12.2f %-14.2f %-14.2f %-14.2f %-14.2f %-14.2f%n",
                        c.id, c.interArrivalTime, c.arrivalTime, c.serviceTime, c.serviceStartTime, c.serviceEndTime, c.waitingTime);
            }
        }

        
        System.out.println("=====================================================================================");
        System.out.println("                                QUEUE STATISTICS                                     ");
        System.out.println("=====================================================================================");
        double totalSimulationTime = customers.get(customers.size() - 1).serviceEndTime;
        double avgWaitingTime = totalWaitingTime / numCustomers;
        double avgTimeInSystem = totalTimeInSystem / numCustomers;
        double serverUtilization = ((totalSimulationTime - totalIdleTime) / totalSimulationTime) * 100;

        System.out.printf("Total Simulation Time    : %.2f minutes%n", totalSimulationTime);
        System.out.printf("Average Waiting Time     : %.2f minutes%n", avgWaitingTime);
        System.out.printf("Average Time in System   : %.2f minutes%n", avgTimeInSystem);
        System.out.printf("Total Server Idle Time   : %.2f minutes%n", totalIdleTime);
        System.out.printf("Server Utilization       : %.2f%%\n", serverUtilization);
        System.out.println("=====================================================================================");
        
        scanner.close();
    }
}
