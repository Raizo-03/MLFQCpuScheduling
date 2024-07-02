package cpuscheduler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class InputScreen extends JFrame {
    private JTextField processNameField;
    private JComboBox<String> processTypeBox;
    private JTextField arrivalTimeField;
    private JTextField burstTimeField;
    private JTable processTable;
    private DefaultTableModel tableModel;
    private List<Process> processList;
    private JComboBox<String> predefinedInputsBox;

    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font INPUT_FONT = new Font("Arial", Font.PLAIN, 12);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font TABLE_HEADER_FONT = new Font("Arial", Font.BOLD, 12);
    private static final Font TABLE_FONT = new Font("Arial", Font.PLAIN, 12);

    public InputScreen() {
        setTitle("CPU Scheduling Simulator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        processList = new ArrayList<>();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Process Name
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel processNameLabel = new JLabel("Process Name");
        processNameLabel.setFont(LABEL_FONT);
        inputPanel.add(processNameLabel, gbc);
        gbc.gridx = 1;
        processNameField = new JTextField(10);
        processNameField.setFont(INPUT_FONT);
        inputPanel.add(processNameField, gbc);

        // Type of Process
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel processTypeLabel = new JLabel("Type of Process");
        processTypeLabel.setFont(LABEL_FONT);
        inputPanel.add(processTypeLabel, gbc);
        gbc.gridx = 1;
        processTypeBox = new JComboBox<>(new String[]{"system", "interactive", "batch"});
        processTypeBox.setFont(INPUT_FONT);
        inputPanel.add(processTypeBox, gbc);

        // Arrival Time
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel arrivalTimeLabel = new JLabel("Arrival Time");
        arrivalTimeLabel.setFont(LABEL_FONT);
        inputPanel.add(arrivalTimeLabel, gbc);
        gbc.gridx = 1;
        arrivalTimeField = new JTextField(10);
        arrivalTimeField.setFont(INPUT_FONT);
        inputPanel.add(arrivalTimeField, gbc);

        // Burst Time
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel burstTimeLabel = new JLabel("Burst Time");
        burstTimeLabel.setFont(LABEL_FONT);
        inputPanel.add(burstTimeLabel, gbc);
        gbc.gridx = 1;
        burstTimeField = new JTextField(10);
        burstTimeField.setFont(INPUT_FONT);
        inputPanel.add(burstTimeField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = createStyledButton("Add Process", new Color(144, 238, 144));
        JButton deleteButton = createStyledButton("Delete Process", new Color(144, 238, 144));
        JButton simulateButton = createStyledButton("SIMULATE", new Color(0, 255, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(simulateButton);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        inputPanel.add(buttonPanel, gbc);

        // Predefined Inputs
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 1;
        JLabel predefinedInputsLabel = new JLabel("Predefined Inputs:");
        predefinedInputsLabel.setFont(LABEL_FONT);
        inputPanel.add(predefinedInputsLabel, gbc);
        gbc.gridx = 1;
        predefinedInputsBox = new JComboBox<>(new String[]{"Set 1", "Set 2", "Set 3"});
        predefinedInputsBox.setFont(INPUT_FONT);
        inputPanel.add(predefinedInputsBox, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
                new String[]{"PROCESS NAME", "TYPE OF PROC.", "ARRIVAL TIME", "BURST TIME"}, 0);
        processTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
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

        JScrollPane scrollPane = new JScrollPane(processTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        // Add action listeners
        addButton.addActionListener(e -> addProcess());
        deleteButton.addActionListener(e -> deleteSelectedProcess());
        simulateButton.addActionListener(e -> startSimulation());
        predefinedInputsBox.addActionListener(e -> loadPredefinedInputs());
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setFont(BUTTON_FONT);
        return button;
    }

    private void addProcess() {
        try {
            String name = processNameField.getText();
            String type = (String) processTypeBox.getSelectedItem();
            int arrivalTime = Integer.parseInt(arrivalTimeField.getText());
            int burstTime = Integer.parseInt(burstTimeField.getText());

            Process process = new Process(name, type, arrivalTime, burstTime);
            processList.add(process);
            addToTable(process);

            // Clear input fields
            processNameField.setText("");
            arrivalTimeField.setText("");
            burstTimeField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter numeric values for arrival and burst times.");
        }
    }

    private void deleteSelectedProcess() {
        int selectedRow = processTable.getSelectedRow();
        if (selectedRow != -1) {
            processList.remove(selectedRow);
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a process to delete.");
        }
    }

    private void startSimulation() {
        new SimulatorScreen(processList).setVisible(true);
        this.dispose();
    }

    private void loadPredefinedInputs() {
        processList.clear();
        String selectedSet = (String) predefinedInputsBox.getSelectedItem();

        switch (selectedSet) {
            case "Set 1":
                processList.add(new Process("P1", "system", 4, 18));
                processList.add(new Process("P2", "interactive", 44, 12));
                processList.add(new Process("P3", "batch", 5, 13));
                processList.add(new Process("P4", "interactive", 12, 6));
                processList.add(new Process("P5", "system", 2, 9));
                processList.add(new Process("P6", "system", 53, 10));
                processList.add(new Process("P7", "batch", 20, 3));
                processList.add(new Process("P8", "system", 10, 17));
                processList.add(new Process("P9", "batch", 1, 14));
                processList.add(new Process("P10", "interactive", 30, 18));
                processList.add(new Process("P11", "system", 3, 15));
                processList.add(new Process("P12", "batch", 9, 51));
                processList.add(new Process("P13", "interactive", 12, 4));
                processList.add(new Process("P14", "system", 3, 3));
                processList.add(new Process("P15", "system", 5, 2));
                break;
            case "Set 2":
                processList.add(new Process("P1", "batch", 0, 12));
                processList.add(new Process("P2", "batch", 0, 6));
                processList.add(new Process("P3", "batch", 2, 7));
                processList.add(new Process("P4", "batch", 5, 10));
                processList.add(new Process("P5", "batch", 0, 5));
                processList.add(new Process("P6", "interactive", 6, 8));
                processList.add(new Process("P7", "interactive", 12, 5));
                processList.add(new Process("P8", "interactive", 20, 13));
                processList.add(new Process("P9", "interactive", 0, 15));
                processList.add(new Process("P10", "interactive", 12, 2));
                
                processList.add(new Process("P21", "system", 15, 9));
                processList.add(new Process("P22", "system", 20, 6));
                processList.add(new Process("P23", "system", 20, 1));
                processList.add(new Process("P24", "system", 0, 4));
                processList.add(new Process("P25", "system", 16, 6));
                break;
            case "Set 3":
                // Add processes for Set 3
                break;
        }

        updateTable();
    }

    private void addToTable(Process process) {
        tableModel.addRow(new Object[]{process.getName(), process.getType(), process.getArrivalTime(),
                process.getBurstTime()});
    }

    private void updateTable() {
        tableModel.setRowCount(0); // Clear the table
        for (Process process : processList) {
            addToTable(process);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InputScreen inputScreen = new InputScreen();
            inputScreen.setVisible(true);
        });
    }
}
