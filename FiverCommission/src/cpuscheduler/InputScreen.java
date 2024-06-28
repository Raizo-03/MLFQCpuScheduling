package cpuscheduler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InputScreen extends JFrame {
    private JTextField numProcessesField;
    private JComboBox<String> processTypeBox;
    private JTextField arrivalTimeField;
    private JTextField burstTimeField;
    private JTextArea processListArea;
    private List<Process> processList;
    private JComboBox<String> predefinedInputsBox;

    public InputScreen() {
        setTitle("Input Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        processList = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));

        JLabel numProcessesLabel = new JLabel("Number of Processes:");
        numProcessesField = new JTextField();
        inputPanel.add(numProcessesLabel);
        inputPanel.add(numProcessesField);

        JLabel processTypeLabel = new JLabel("Type of Process:");
        processTypeBox = new JComboBox<>(new String[]{"real-time", "system", "interactive", "batch"});
        inputPanel.add(processTypeLabel);
        inputPanel.add(processTypeBox);

        JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
        arrivalTimeField = new JTextField();
        inputPanel.add(arrivalTimeLabel);
        inputPanel.add(arrivalTimeField);

        JLabel burstTimeLabel = new JLabel("Burst Time:");
        burstTimeField = new JTextField();
        inputPanel.add(burstTimeLabel);
        inputPanel.add(burstTimeField);

        JButton addButton = new JButton("Add Process");
        inputPanel.add(addButton);

        JButton startSimulationButton = new JButton("Start Simulation");
        inputPanel.add(startSimulationButton);

        panel.add(inputPanel, BorderLayout.NORTH);

        processListArea = new JTextArea();
        processListArea.setEditable(false);
        panel.add(new JScrollPane(processListArea), BorderLayout.CENTER);

        add(panel);
        
        JLabel predefinedInputsLabel = new JLabel("Predefined Inputs:");
        predefinedInputsBox = new JComboBox<>(new String[]{"Scenario 1", "Scenario 2", "Scenario 3"});
        inputPanel.add(predefinedInputsLabel);
        inputPanel.add(predefinedInputsBox);

        
        predefinedInputsBox.addActionListener(e -> loadPredefinedInputs());

        
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProcess();
            }
        });

        startSimulationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation();
            }
        });
    }
    private void loadPredefinedInputs() {
        processList.clear(); // Clear any existing processes
        processListArea.setText(""); // Clear the display area

        String selectedScenario = (String) predefinedInputsBox.getSelectedItem();
        if (selectedScenario.equals("Scenario 1")) {
            processList.add(new Process("P1", "real-time", 0, 4));
            processList.add(new Process("P2", "system", 1, 3));
            processList.add(new Process("P3", "interactive", 2, 5));
            processList.add(new Process("P4", "batch", 3, 2));
            processList.add(new Process("P5", "real-time", 4, 1));
            processList.add(new Process("P6", "system", 5, 4));
            processList.add(new Process("P7", "interactive", 6, 6));
            processList.add(new Process("P8", "batch", 7, 3));
            processList.add(new Process("P9", "real-time", 8, 2));
            processList.add(new Process("P10", "system", 9, 1));
            processList.add(new Process("P11", "interactive", 10, 3));
            processList.add(new Process("P12", "batch", 11, 4));
            processList.add(new Process("P13", "real-time", 12, 5));
            processList.add(new Process("P14", "system", 13, 2));
            processList.add(new Process("P15", "interactive", 14, 3));
        } else if (selectedScenario.equals("Scenario 2")) {
            processList.add(new Process("P1", "interactive", 1, 4));
            processList.add(new Process("P2", "batch", 3, 6));
            // Add more processes as needed
        }
        // Add more scenarios as required

        // Display the loaded processes in the processListArea
        for (Process process : processList) {
            processListArea.append(process + "\n");
        }
    }

    private void addProcess() {
        try {
            String type = (String) processTypeBox.getSelectedItem();
            int arrivalTime = Integer.parseInt(arrivalTimeField.getText());
            int burstTime = Integer.parseInt(burstTimeField.getText());
            int processNumber = processList.size() + 1;
            Process process = new Process("P" + processNumber, type, arrivalTime, burstTime);
            processList.add(process);

            processListArea.append(process + "\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for arrival and burst times.");
        }
    }

    private void startSimulation() {
        new SimulatorScreen(processList).setVisible(true);
        dispose();
    }
}
