import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class SupermarketSimulationGUI extends JFrame {
    private List<CashCounter> counters;
    private JTextField numCountersField, processingTimeField, customerIntervalField, totalCustomersField;
    private JTextArea statusArea;
    private JButton startButton;
    private JComboBox<String> assignmentStrategyBox;
    private int processingTime, customerInterval, totalCustomers;
    private Timer timer;
    private int time = 0;
    private int customerCount = 0;

    public SupermarketSimulationGUI() {
        setTitle("Supermarket Queue Simulation");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        numCountersField = new JTextField();
        processingTimeField = new JTextField();
        customerIntervalField = new JTextField();
        totalCustomersField = new JTextField();
        assignmentStrategyBox = new JComboBox<>(new String[]{"Random", "Shortest Queue"});
        
        inputPanel.add(new JLabel("Number of Cash Counters:"));
        inputPanel.add(numCountersField);
        inputPanel.add(new JLabel("Customer Processing Time (min):"));
        inputPanel.add(processingTimeField);
        inputPanel.add(new JLabel("Customer Arrival Rate (cust/min):"));
        inputPanel.add(customerIntervalField);
        inputPanel.add(new JLabel("Total Number of Customers:"));
        inputPanel.add(totalCustomersField);
        inputPanel.add(new JLabel("Assignment Strategy:"));
        inputPanel.add(assignmentStrategyBox);

        statusArea = new JTextArea();
        statusArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statusArea);

        startButton = new JButton("Start Simulation");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupSimulation();
            }
        });

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(startButton, BorderLayout.SOUTH);
    }

    private void setupSimulation() {
        int numCounters = Integer.parseInt(numCountersField.getText());
        processingTime = Integer.parseInt(processingTimeField.getText());
        customerInterval = Integer.parseInt(customerIntervalField.getText());
        totalCustomers = Integer.parseInt(totalCustomersField.getText());
        String assignmentStrategy = (String) assignmentStrategyBox.getSelectedItem();

        counters = new ArrayList<>();
        for (int i = 0; i < numCounters; i++) {
            counters.add(new CashCounter());
        }
        statusArea.setText("");
        customerCount = 0;
        time = 0;

        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runSimulationStep(assignmentStrategy);
            }
        });
        timer.start();
    }

    private void runSimulationStep(String assignmentStrategy) {
        if (customerCount < totalCustomers) {
            if (time % customerInterval == 0) {
                assignCustomerToCounter(assignmentStrategy, processingTime);
                customerCount++;
            }
        }
        counters.forEach(counter -> counter.processCustomer(processingTime));
        updateStatusArea();
        time++;
        
        if (customerCount >= totalCustomers && allCountersEmpty()) {
            timer.stop();
            displayFinalResults();
        }
    }

    private void assignCustomerToCounter(String assignmentStrategy, int processingTime) {
    if ("Random".equals(assignmentStrategy)) {
        int index = new Random().nextInt(counters.size());
        counters.get(index).addCustomer(processingTime);
    } else if ("Shortest Queue".equals(assignmentStrategy)) {
        CashCounter shortest = counters.get(0);
        for (CashCounter counter : counters) {
            if (counter.getQueueLength() < shortest.getQueueLength()) {
                shortest = counter;
            }
        }
        shortest.addCustomer(processingTime);
    }
}


    private boolean allCountersEmpty() {
        return counters.stream().allMatch(counter -> counter.getQueueLength() == 0);
    }

    private void updateStatusArea() {
        StringBuilder statusBuilder = new StringBuilder();
        statusBuilder.append("Time: ").append(time).append(" minutes\n");
        for (int i = 0; i < counters.size(); i++) {
            statusBuilder.append("Counter ").append(i + 1).append(": ")
                         .append(counters.get(i).getQueueLength()).append(" customers waiting\n");
        }
        statusArea.setText(statusBuilder.toString());
    }

    private void displayFinalResults() {
        StringBuilder resultsBuilder = new StringBuilder(statusArea.getText());
        resultsBuilder.append("\nSimulation finished. Final results:\n");
        for (int i = 0; i < counters.size(); i++) {
            resultsBuilder.append("Counter ").append(i + 1).append(":\n")
                          .append("Total customers processed: ").append(counters.get(i).getTotalProcessed()).append("\n")
                          .append("Average waiting time: ").append(counters.get(i).getAverageWaitingTime()).append(" minutes\n");
        }
        statusArea.setText(resultsBuilder.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SupermarketSimulationGUI().setVisible(true);
            }
        });
    }
}

class CashCounter {
    private Queue<Integer> queue = new LinkedList<>();
    private int totalProcessed = 0;
    private long totalWaitingTime = 0;

    public void addCustomer(int processingTime) {
        queue.offer(processingTime);
    }

    public void processCustomer(int timePerCustomer) {
    if (!queue.isEmpty()) {
        int remainingTime = queue.peek() - timePerCustomer;
        queue.poll(); // Remove the customer being processed.
        if (remainingTime > 0) {
            // If the customer wasn't fully processed, add them back with the remaining time.
            queue.offer(remainingTime);
        }
        totalProcessed++;
        totalWaitingTime += timePerCustomer; // This should be adjusted to reflect actual waiting time.
    }
}


    public int getQueueLength() {
        return queue.size();
    }

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public double getAverageWaitingTime() {
        return totalProcessed == 0 ? 0 : (double) totalWaitingTime / totalProcessed;
    }
}
