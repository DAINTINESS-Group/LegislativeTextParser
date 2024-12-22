package org.uoi.legislativetextparser.gui;

import org.json.JSONObject;
import org.uoi.legislativetextparser.config.Config;
import org.uoi.legislativetextparser.engine.LawProcessor;
import org.uoi.legislativetextparser.entityextraction.EntityExtractor;
import org.uoi.legislativetextparser.entityextraction.ManualEntityExtractor;
import org.uoi.legislativetextparser.entityextraction.SpecificLocationEntityExtractor;
import org.uoi.legislativetextparser.tree.LawTreeBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class MainMenu {

    public void createMainMenu() {
        JFrame frame = new JFrame("Legislative Text Parser - Entry Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel inputLabel = new JLabel("Input PDF Path:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(inputLabel, gbc);

        JTextField inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(250, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        inputPanel.add(inputField, gbc);

        JButton inputBrowseButton = new JButton("Browse");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        inputPanel.add(inputBrowseButton, gbc);

        JPanel outputPanel = new JPanel(new GridBagLayout());

        JLabel outputLabel = new JLabel("Output JSON Path:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        outputPanel.add(outputLabel, gbc);

        JTextField outputField = new JTextField();
        outputField.setPreferredSize(new Dimension(250, 25));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        outputPanel.add(outputField, gbc);

        JButton outputBrowseButton = new JButton("Browse");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        outputPanel.add(outputBrowseButton, gbc);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(outputPanel);

        JCheckBox definitionCheckbox = new JCheckBox("Specify Definitions Location");

        JPanel definitionForm = new JPanel(new GridLayout(2, 2, 5, 5));
        definitionForm.setBorder(BorderFactory.createTitledBorder("Definitions Location"));
        definitionForm.setVisible(false);
        JLabel chapterLabel = new JLabel("Chapter Number: ");
        JTextField chapterField = new JTextField(5);
        JLabel articleLabel = new JLabel("Article Number:");
        JTextField articleField = new JTextField(5);
        definitionForm.add(chapterLabel);
        definitionForm.add(chapterField);
        definitionForm.add(articleLabel);
        definitionForm.add(articleField);

        definitionCheckbox.addActionListener(e -> definitionForm.setVisible(definitionCheckbox.isSelected()));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton startButton = new JButton("Start");
        JButton exitButton = new JButton("Exit");
        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(outputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(definitionCheckbox);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(definitionForm);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);

        frame.add(mainPanel, BorderLayout.CENTER);

        inputBrowseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                inputField.setText(selectedFile.getAbsolutePath());
            }
        });

        outputBrowseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValue = fileChooser.showSaveDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                outputField.setText(selectedFile.getAbsolutePath());
            }
        });

        startButton.addActionListener(e -> {
            String inputPath = inputField.getText().trim();
            String outputPath = outputField.getText().trim();

            if (inputPath.isEmpty() || outputPath.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please specify both input and output paths.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                Config config = new Config(inputPath, outputPath);
                try {
                    LawProcessor lawProcessor = new LawProcessor(config);
                    lawProcessor.processLegislativeDocument();

                    EntityExtractor extractor;
                    if (definitionCheckbox.isSelected()) {
                        int chapterNumber = Integer.parseInt(chapterField.getText().trim());
                        int articleNumber = Integer.parseInt(articleField.getText().trim());
                        extractor = new SpecificLocationEntityExtractor(chapterNumber, articleNumber);
                    } else {
                        extractor = new ManualEntityExtractor();
                    }

                    List<String> entities = extractor.extractEntities(outputPath);
                    if (entities.isEmpty()) {
                        JOptionPane.showMessageDialog(frame, "No entities found in the specified location.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    JOptionPane.showMessageDialog(frame, "Processing completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    List<String> capitalizedEntities = entities.stream()
                            .map(c -> c.substring(0, 1).toUpperCase() + c.substring(1)).collect(Collectors.toList());
//                            .toList();

                    EntityVisualizer entityVisualizer = new EntityVisualizer();
                    entityVisualizer.displayEntities(capitalizedEntities);

                    String jsonContent = Files.readString(Paths.get(outputPath));
                    JSONObject jsonObject = new JSONObject(jsonContent);

                    TreeVisualizer treeVisualizer = new TreeVisualizer();
                    treeVisualizer.displayTree(LawTreeBuilder.buildTree(jsonObject), entityVisualizer.getFrame());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "An error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        exitButton.addActionListener(e -> System.exit(0));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
