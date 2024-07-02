package cpuscheduler;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GanttChartPanel extends JPanel {
    private final Map<String, Color> processColors = new HashMap<>();
    private final List<ProcessExecution> executions = new ArrayList<>();
    private int nextColorIndex = 0;
    private int currentBlock = 0; // Track the number of blocks

    private static final Color[] COLORS = {
            Color.RED, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN, Color.MAGENTA,
            Color.PINK, Color.YELLOW, Color.LIGHT_GRAY, Color.DARK_GRAY
    };

    public GanttChartPanel() {
        setPreferredSize(new Dimension(1000, 60)); // Initial preferred size
    }

    public void addProcessExecution(String processName, int startTime, int burstTime) {
        executions.add(new ProcessExecution(processName, startTime, burstTime));
        int width = (startTime + burstTime) * 30 + 20; // Calculate new width
        if (width > getPreferredSize().width) {
            setPreferredSize(new Dimension(width, 60)); // Update preferred size if needed
        }
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int height = getHeight();
        int processHeight = height / 2;
        int y = processHeight / 2 - 10;

        int scaleFactor = 30; // Adjust this value to scale the time units better
        int spaceBetweenBlocks = 5; // Space between the blocks
        int startX = 10; // Starting X position to leave some space on the left

        currentBlock = 0; // Reset current block count

        for (ProcessExecution execution : executions) {
            Color color = processColors.computeIfAbsent(execution.processName, k -> COLORS[nextColorIndex++ % COLORS.length]);
            g.setColor(color);

            int x = startX + execution.startTime * scaleFactor;
            int w = Math.max(execution.burstTime * scaleFactor, g.getFontMetrics().stringWidth(execution.processName) + 20); // Increase width a bit more
            int h = 650; // Increase height

            g.fillRect(x, y, w - spaceBetweenBlocks, h);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, w - spaceBetweenBlocks, h);
            g.drawString(execution.processName, x + 5, y + h / 2 + 5);

            currentBlock++; // Increment current block count
        }
    }

    public int getCurrentBlock() {
        return currentBlock;
    }

    private static class ProcessExecution {
        final String processName;
        final int startTime;
        final int burstTime;

        ProcessExecution(String processName, int startTime, int burstTime) {
            this.processName = processName;
            this.startTime = startTime;
            this.burstTime = burstTime;
        }
    }
}
