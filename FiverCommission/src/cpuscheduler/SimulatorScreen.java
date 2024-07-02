package cpuscheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulatorScreen extends JFrame {
    private int clock = 0;
    private Timer timer;
    private List<Process> processList;
    private DefaultTableModel tableModel;
    private GanttChartPanel ganttChart1;
    private GanttChartPanel ganttChart2;
    private JTable processTable;
    private List<Process> processor1Queue;
    private List<Process> processor2Queue;

    private JLabel currentProcess1Label;
    private JLabel currentProcess2Label;
    private JLabel currentTimeLabel;
    private JLabel utilizationLabel;
    private JPanel readyQueuePanel;
    private JPanel cpuPanel;

    private double systemsSRTFAWT;
    private double systemsSRTFATT;
    private double interactiveRRAWT;
    private double interactiveRRATT;
    private double batchFCFSAWT;
    private double batchFCFSATT;

    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font TABLE_HEADER_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font TABLE_FONT = new Font("Arial", Font.PLAIN, 12);

    public SimulatorScreen(List<Process> processList) {
        this.processList = processList;
        this.processor1Queue = new ArrayList<>();
        this.processor2Queue = new ArrayList<>();

        setTitle("Simulator Screen");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Modern Clock Panel
        JPanel clockPanel = new JPanel();
        clockPanel.setBackground(new Color(30, 30, 30));
        clockPanel.setPreferredSize(new Dimension(1000, 40));
        JLabel clockLabel = new JLabel("Clock: 0", SwingConstants.CENTER);
        clockLabel.setFont(new Font("Arial", Font.BOLD, 30));
        clockLabel.setForeground(Color.WHITE);
        clockPanel.add(clockLabel);
        panel.add(clockPanel, BorderLayout.NORTH);

        // Enlarged CPU Panel
        cpuPanel = new JPanel(new GridLayout(6, 1));
        cpuPanel.setBorder(BorderFactory.createTitledBorder("CPU"));
        cpuPanel.setPreferredSize(new Dimension(300, 350));

        currentProcess1Label = new JLabel("Current Process 1: None");
        currentProcess2Label = new JLabel("Current Process 2: None");
        currentTimeLabel = new JLabel("Current Time: 0");
        utilizationLabel = new JLabel("Utilization: 0%");

        currentProcess1Label.setFont(TABLE_HEADER_FONT);
        currentProcess2Label.setFont(TABLE_HEADER_FONT);
        currentTimeLabel.setFont(TABLE_HEADER_FONT);
        utilizationLabel.setFont(TABLE_HEADER_FONT);

        cpuPanel.add(currentProcess1Label);
        cpuPanel.add(currentProcess2Label);
        cpuPanel.add(currentTimeLabel);
        cpuPanel.add(utilizationLabel);

        // Smaller Ready Queue Panel
        readyQueuePanel = new JPanel();
        readyQueuePanel.setPreferredSize(new Dimension(250, 150));

        readyQueuePanel.setBorder(BorderFactory.createTitledBorder("Ready Queue"));
        readyQueuePanel.setLayout(new GridLayout(1, 10, 5, 5)); // Adjust grid layout as necessary
        
        // Main Display Panel
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(cpuPanel, BorderLayout.WEST);
        displayPanel.add(readyQueuePanel, BorderLayout.EAST);

        // Add table to display process properties
        String[] columnNames = {"Process Name", "Type", "Arrival Time", "Burst Time", "Remaining Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(tableModel);
        processTable.setFont(TABLE_FONT);
        processTable.setRowHeight(25);
        processTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set white background and remove grid lines
        processTable.setBackground(Color.WHITE);
        processTable.setShowGrid(false);

        JTableHeader header = processTable.getTableHeader();
        header.setFont(TABLE_HEADER_FONT);
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);

        JScrollPane tableScrollPane = new JScrollPane(processTable);

        // Gantt Chart Panel
        JPanel ganttChartPanel = new JPanel();
        ganttChartPanel.setLayout(new GridLayout(2, 1));

        JLabel processor1Label = new JLabel("Processor 1:");
        processor1Label.setFont(new Font("Arial", Font.BOLD, 20));
        processor1Label.setForeground(Color.BLACK);

        ganttChart1 = new GanttChartPanel();
        JScrollPane scrollPane1 = new JScrollPane(ganttChart1);
        scrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        ganttChartPanel.add(processor1Label);
        ganttChartPanel.add(scrollPane1);

        JLabel processor2Label = new JLabel("Processor 2:");
        processor2Label.setFont(new Font("Arial", Font.BOLD, 20));
        processor2Label.setForeground(Color.BLACK);

        ganttChart2 = new GanttChartPanel();
        JScrollPane scrollPane2 = new JScrollPane(ganttChart2);
        scrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        ganttChartPanel.add(processor2Label);
        ganttChartPanel.add(scrollPane2);

        // Adding components to the main panel
        panel.add(displayPanel, BorderLayout.WEST);
        panel.add(tableScrollPane, BorderLayout.EAST);
        panel.add(ganttChartPanel, BorderLayout.CENTER);

        // Control Panel
        JPanel controlPanel = new JPanel();
        JButton playButton = createStyledButton("Play", new Color(144, 238, 144));
        JButton stopButton = createStyledButton("Stop", new Color(255, 99, 71));

        controlPanel.add(playButton);
        controlPanel.add(stopButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        add(panel);

        playButton.addActionListener(e -> startClock(clockLabel, scrollPane1, scrollPane2));
        stopButton.addActionListener(e -> stopClock());

        initializeTable();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        return button;
    }

    private void initializeTable() {
        for (Process process : processList) {
            tableModel.addRow(new Object[]{process.getName(), process.getType(), process.getArrivalTime(), process.getBurstTime(), process.getRemainingTime(), process.getStatus()});
        }
    }

    private void startClock(JLabel clockLabel, JScrollPane scrollPane1, JScrollPane scrollPane2) {
        timer = new Timer(1000, e -> {
            clock++;
            clockLabel.setText("Clock: " + clock);
            updateSimulation(scrollPane1, scrollPane2);
        });
        timer.start();
    }

    private void stopClock() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void updateSimulation(JScrollPane scrollPane1, JScrollPane scrollPane2) {
        // Update Ready Queue display
        readyQueuePanel.removeAll();
        for (Process process : processList) {
            if (process.getArrivalTime() <= clock && process.getRemainingTime() > 0) {
                JLabel processLabel = new JLabel(process.getName(), SwingConstants.CENTER);
                processLabel.setOpaque(true);
                processLabel.setBackground(process.getType().equals("batch") ? Color.GREEN : Color.ORANGE);
                processLabel.setPreferredSize(new Dimension(60, 20)); // Smaller boxes for Ready Queue
                readyQueuePanel.add(processLabel);
            }
        }
        readyQueuePanel.revalidate();
        readyQueuePanel.repaint();

        // Simulate scheduling and execution
        simulateScheduling(scrollPane1, scrollPane2);

        // Update Process Table
        updateProcessTable();

        // Check if all processes have finished
        if (areAllProcessesFinished()) {
            checkAllProcessesFinished(); // Call only once when all processes are finished

        }
    }

    private void simulateScheduling(JScrollPane scrollPane1, JScrollPane scrollPane2) {
        // Clear current queues
        processor1Queue.clear();
        processor2Queue.clear();

        // Separate processes into respective processor queues
        for (Process process : processList) {
            if (process.getArrivalTime() <= clock && process.getRemainingTime() > 0) {
                if (process.getType().equals("batch")) {
                    processor1Queue.add(process);
                } else {
                    processor2Queue.add(process);
                }
            }
        }

        // Sort processor queues based on scheduling algorithm
        processor1Queue.sort(Comparator.comparingInt(Process::getArrivalTime)); // FCFS for batch
        processor2Queue.sort(Comparator.comparingInt(Process::getRemainingTime)); // SRTF for system

        // Execute processes
        if (!processor1Queue.isEmpty()) {
            Process process = processor1Queue.get(0);
            executeProcess(process, ganttChart1, scrollPane1, 1);
        } else {
            currentProcess1Label.setText("Current Process 1: None");
        }

        if (!processor2Queue.isEmpty()) {
            Process process = processor2Queue.get(0);
            executeProcess(process, ganttChart2, scrollPane2, 2);
        } else {
            currentProcess2Label.setText("Current Process 2: None");
        }
    }

    private void executeProcess(Process process, GanttChartPanel ganttChart, JScrollPane scrollPane, int processor) {
        if (processor == 1) {
            currentProcess1Label.setText("Current Process 1: " + process.getName());
        } else {
            currentProcess2Label.setText("Current Process 2: " + process.getName());
        }
        currentTimeLabel.setText("Current Time: " + clock);
        utilizationLabel.setText("Utilization: " + ((int) ((1.0 - (double) process.getRemainingTime() / process.getBurstTime()) * 100)) + "%");

        process.setStatus("waiting");
        updateProcessTable();
        ganttChart.addProcessExecution(process.getName(), clock, 1);

        // Adjust scroll position to move to the left to the first 5 blocks, then one step to the right
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        int blockWidth = 30; // Adjust this value based on your scale factor
        int currentBlock = ganttChart.getCurrentBlock(); // Assuming you have a method to get the current block index

        if (currentBlock >= 5) {
            // Move to the left to the first 5 blocks
            horizontalScrollBar.setValue((currentBlock - 12) * blockWidth);
        } else {
            // If fewer than 5 blocks are shown, move to the start
            horizontalScrollBar.setValue(0);
        }

        process.decrementRemainingTime();

        process.setWaitingTime(clock - process.getArrivalTime());
        process.setTurnaroundTime(clock - process.getArrivalTime() + 1); // +1 to include current clock

        System.out.println("Clock: " + (clock - 1) + "-" + clock + ": " + process.getName());

        if (process.getRemainingTime() == 0) {
            process.setStatus("terminated");
        } else {
            process.setStatus("processing");
            process.demote();
        }
        updateProcessTable();
    }



    private void updateProcessTable() {
        tableModel.setRowCount(0);
        for (Process process : processList) {
            tableModel.addRow(new Object[]{process.getName(), process.getType(), process.getArrivalTime(), process.getBurstTime(), process.getRemainingTime(), process.getStatus()});
        }
    }

    private boolean areAllProcessesFinished() {
        for (Process process : processList) {
            if (process.getRemainingTime() > 0) {
                return false;
            }
        }
        return true;
    }

    private void calculateAverageWaitingTime(String type) {
        double sum = 0;
        int count = 0;
        for (Process process : processList) {
            if (process.getType().equalsIgnoreCase(type)) {
                sum += process.getWaitingTime();
                count++;
            }
        }
        double average = count > 0 ? sum / count : 0;
        switch (type) {
            case "system":
                systemsSRTFAWT = average;
                break;
            case "interactive":
                interactiveRRAWT = average;
                break;
            case "batch":
                batchFCFSAWT = average;
                break;
        }
    }

    private void calculateAverageTurnaroundTime(String type) {
        double sum = 0;
        int count = 0;
        for (Process process : processList) {
            if (process.getType().equalsIgnoreCase(type)) {
                sum += process.getTurnaroundTime();
                count++;
            }
        }
        double average = count > 0 ? sum / count : 0;
        switch (type) {
            case "system":
                systemsSRTFATT = average;
                break;
            case "interactive":
                interactiveRRATT = average;
                break;
            case "batch":
                batchFCFSATT = average;
                break;
        }
    }

    private boolean averageTimesPrinted = false; // Define the flag

    private void checkAllProcessesFinished() {
        if (!averageTimesPrinted) {
            calculateAverageWaitingTime("system");
            calculateAverageWaitingTime("interactive");
            calculateAverageWaitingTime("batch");
            
            calculateAverageTurnaroundTime("system");
            calculateAverageTurnaroundTime("interactive");
            calculateAverageTurnaroundTime("batch");
            
            // Print AWT and ATT after all processes are finished
            System.out.println("SYSTEMS (SRTF): AWT = " + String.format("%.2f", systemsSRTFAWT) + "; ATT = " + String.format("%.2f", systemsSRTFATT));
            System.out.println("INTERACTIVE (Round Robin): AWT = " + String.format("%.2f", interactiveRRAWT) + "; ATT = " + String.format("%.2f", interactiveRRATT));
            System.out.println("BATCH (FCFS): AWT = " + String.format("%.2f", batchFCFSAWT) + "; ATT = " + String.format("%.2f", batchFCFSATT));

            averageTimesPrinted = true; // Set flag to true to prevent further printing
        }
    }


    public static void main(String[] args) {
        // Example usage
        List<Process> processes = new ArrayList<>();
        processes.add(new Process("P1", "system", 0, 5));
        processes.add(new Process("P2", "batch", 1, 3));
        processes.add(new Process("P3", "interactive", 2, 6));

        SwingUtilities.invokeLater(() -> {
            SimulatorScreen simulatorScreen = new SimulatorScreen(processes);
            simulatorScreen.setVisible(true);
        });
    }
}
