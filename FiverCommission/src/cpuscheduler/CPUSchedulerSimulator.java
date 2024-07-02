package cpuscheduler;

import javax.swing.*;
import java.awt.*;

public class CPUSchedulerSimulator {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("CPU Scheduling Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.setBackground(Color.WHITE);  // Light gray background

            JLabel titleLabel = new JLabel("CPU Scheduling Simulator", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            panel.add(titleLabel, BorderLayout.NORTH);

            JTextArea objectivesText = new JTextArea();
            objectivesText.setText(
                    "Learning Objectives:\n" +
                            "1. Understand CPU scheduling algorithms.\n" +
                            "2. Learn how multi-level feedback queues (MLFQ) work.\n" +
                            "3. Visualize the execution of processes in a multiprocessor system.\n\n" +
                            "CPU Scheduling Rules:\n" +
                            "- Multi-Level Feedback Queue (MLFQ)\n" +
                            "- Preemption and auto-demote policies\n\n" +
                            "Components of the Simulation:\n" +
                            "- Ready Queue\n" +
                            "- Process Properties\n" +
                            "- Gantt Charts\n" +
                            "- Play & Stop Buttons"
            );
            objectivesText.setEditable(false);
            objectivesText.setLineWrap(true);
            objectivesText.setWrapStyleWord(true);
            objectivesText.setBackground(Color.WHITE);  // Light gray background for text area
            panel.add(objectivesText, BorderLayout.CENTER);

            JButton startButton = new JButton("Start Simulation");
            startButton.setBackground(new Color(144, 238, 144));  // Light green background
            startButton.setFocusPainted(false);
            startButton.setFont(new Font("Arial", Font.BOLD, 12));
            startButton.addActionListener(e -> {
                new InputScreen().setVisible(true);
                frame.dispose();
            });
            panel.add(startButton, BorderLayout.SOUTH);

            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
