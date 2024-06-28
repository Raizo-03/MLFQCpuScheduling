package cpuscheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class SimulatorScreen extends JFrame {
    private int clock = 0;
    private Timer timer;
    private List<Process> processList;
    private JTextArea readyQueueArea;
    private JTextArea ganttChart1;
    private JTextArea ganttChart2;
    private DefaultTableModel tableModel;
    private JTable processTable;
    private List<Process> processor1Queue;
    private List<Process> processor2Queue;

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

        JLabel clockLabel = new JLabel("Clock: 0", SwingConstants.CENTER);
        panel.add(clockLabel, BorderLayout.NORTH);

        readyQueueArea = new JTextArea();
        readyQueueArea.setText("Ready Queue:\n");
        readyQueueArea.setEditable(false);
        panel.add(new JScrollPane(readyQueueArea), BorderLayout.WEST);

        // Add table to display process properties
        String[] columnNames = {"Process Name", "Type", "Arrival Time", "Burst Time", "Remaining Time", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        processTable = new JTable(tableModel);
        panel.add(new JScrollPane(processTable), BorderLayout.EAST);

        JPanel ganttChartPanel = new JPanel();
        ganttChartPanel.setLayout(new GridLayout(2, 1));

        JLabel processor1Label = new JLabel("Processor 1:");
        ganttChart1 = new JTextArea();
        ganttChart1.setEditable(false);
        ganttChartPanel.add(processor1Label);
        ganttChartPanel.add(new JScrollPane(ganttChart1));

        JLabel processor2Label = new JLabel("Processor 2:");
        ganttChart2 = new JTextArea();
        ganttChart2.setEditable(false);
        ganttChartPanel.add(processor2Label);
        ganttChartPanel.add(new JScrollPane(ganttChart2));

        panel.add(ganttChartPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton playButton = new JButton("Play");
        JButton stopButton = new JButton("Stop");

        controlPanel.add(playButton);
        controlPanel.add(stopButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        add(panel);

        playButton.addActionListener(e -> startClock(clockLabel));
        stopButton.addActionListener(e -> stopClock());

        initializeTable();
    }

    private void initializeTable() {
        for (Process process : processList) {
            tableModel.addRow(new Object[]{process.getName(), process.getType(), process.getArrivalTime(), process.getBurstTime(), process.getRemainingTime(), process.getStatus()});
        }
    }

    private void startClock(JLabel clockLabel) {
        timer = new Timer(1000, e -> {
            clock++;
            clockLabel.setText("Clock: " + clock);
            updateSimulation();
        });
        timer.start();
    }

    private void stopClock() {
        if (timer != null) {
            timer.stop();
        }
    }

    private void updateSimulation() {
        // Update Ready Queue display
        StringBuilder readyQueueText = new StringBuilder("Ready Queue:\n");
        for (Process process : processList) {
            if (process.getArrivalTime() <= clock && process.getRemainingTime() > 0) {
                readyQueueText.append(process).append("\n");
            }
        }
        readyQueueArea.setText(readyQueueText.toString());

        // Simulate scheduling and execution
        simulateScheduling();

        // Update Gantt charts
        updateGanttCharts();

        // Update Process Table
        updateProcessTable();
    }

    private void simulateScheduling() {
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
            executeProcess(process, ganttChart1);
        }

        if (!processor2Queue.isEmpty()) {
            Process process = processor2Queue.get(0);
            executeProcess(process, ganttChart2);
        }
    }

    private void executeProcess(Process process, JTextArea ganttChart) {
        process.setStatus("processing");
        process.decrementRemainingTime();
        ganttChart.append(process.getName() + " ");

        if (process.getRemainingTime() == 0) {
            process.setStatus("terminated");
        } else {
            process.setStatus("waiting");
            process.demote();
        }
    }

    private void updateGanttCharts() {
        // Already updating in simulateScheduling() -> executeProcess()
    }

    private void updateProcessTable() {
        tableModel.setRowCount(0);
        for (Process process : processList) {
            tableModel.addRow(new Object[]{process.getName(), process.getType(), process.getArrivalTime(), process.getBurstTime(), process.getRemainingTime(), process.getStatus()});
        }
    }
}
