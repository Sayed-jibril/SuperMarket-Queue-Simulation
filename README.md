# Supermarket Queue Simulation

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)

This Java program simulates a supermarket queue system with multiple cash counters. It allows you to specify the number of cash counters, processing time for each customer, arrival rate of customers, total number of customers, and the assignment strategy for customers to join queues.

## How to Use

1. **Input Parameters**:
   - Number of Cash Counters: Enter the desired number of cash counters.
   - Customer Processing Time (min): Set the time required to process each customer.
   - Customer Arrival Rate (cust/min): Specify the rate at which customers arrive.
   - Total Number of Customers: Enter the total number of customers to be simulated.
   - Assignment Strategy: Choose between "Random" and "Shortest Queue" strategies for assigning customers to counters.

2. **Start Simulation**:
   Click the "Start Simulation" button to begin the simulation with the specified parameters.

3. **Simulation Progress**:
   - The simulation will display the status, including the time elapsed and the number of customers waiting at each counter, in real-time.

4. **Final Results**:
   - After the simulation completes, the program will show the final results for each counter, including the total customers processed and the average waiting time.

## Implementation Details

The simulation is implemented using Java Swing for the GUI. It creates a JFrame with input fields for setting simulation parameters and displays simulation status using JTextArea.

## Running the Program

To run the simulation, execute the `main` method in the `SupermarketSimulationGUI` class. The GUI will appear, allowing you to set parameters and start the simulation.

```java
public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new SupermarketSimulationGUI().setVisible(true);
        }
    });
}

