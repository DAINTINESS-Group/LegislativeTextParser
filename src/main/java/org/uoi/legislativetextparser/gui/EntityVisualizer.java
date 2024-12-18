package org.uoi.legislativetextparser.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EntityVisualizer {

    /**
     * Displays the list of entities in a Swing GUI.
     *
     * @param entities List of entities to display.
     */
    public void displayEntities(List<String> entities) {

        JFrame frame = new JFrame("Entity Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Extracted Entities", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel, BorderLayout.NORTH);

        JList<String> entityList = new JList<>(entities.toArray(new String[0]));
        entityList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(entityList);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
