package org.uoi.legislativetextparser.gui;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class EntityVisualizer {

    private JFrame frame;

    public JFrame getFrame() {
        return this.frame;
    }

    /**
     * Displays the list of entities in a Swing GUI.
     *
     * @param entities List of entities to display.
     */
    public void displayEntities(List<String> entities) {
        this.frame = new JFrame("Entity Visualizer");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setSize(400, 600);
        this.frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Extracted Entities", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.frame.add(titleLabel, BorderLayout.NORTH);

        JList<String> entityList = new JList<>(entities.toArray(new String[0]));
        entityList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane scrollPane = new JScrollPane(entityList);
        this.frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save as JSON");
        JButton closeButton = new JButton("Close");

        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save Entities as JSON");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int userSelection = fileChooser.showSaveDialog(this.frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().endsWith(".json")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".json");
                }

                try {
                    saveEntitiesAsJson(entities, fileToSave);
                    JOptionPane.showMessageDialog(this.frame, "Entities saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this.frame, "Failed to save entities: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        closeButton.addActionListener(e -> this.frame.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(closeButton);

        this.frame.add(buttonPanel, BorderLayout.SOUTH);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
    }

    /**
     * Saves the entities to a JSON file.
     *
     * @param entities List of entities to save.
     * @param file     File to save the entities in.
     * @throws Exception If an error occurs during file writing.
     */
    private void saveEntitiesAsJson(List<String> entities, File file) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, entities);
    }
}
