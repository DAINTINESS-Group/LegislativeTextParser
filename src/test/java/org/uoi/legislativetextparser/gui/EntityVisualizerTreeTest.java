package org.uoi.legislativetextparser.gui;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityVisualizerTreeTest {

    @Test
    public void testDisplayEntities() {
        EntityVisualizer visualizer = new EntityVisualizer();

        List<String> sampleEntities = Arrays.asList("Entity1", "Entity2", "Entity3");

        visualizer.displayEntities(sampleEntities);

        JFrame frame = visualizer.getFrame();
        assertNotNull(frame, "Frame should not be null");
        assertEquals("Entity Visualizer", frame.getTitle(), "Frame title should be 'Entity Visualizer'");
        assertEquals(400, frame.getWidth(), "Frame width should be 400");
        assertEquals(600, frame.getHeight(), "Frame height should be 600");
        assertEquals(JFrame.EXIT_ON_CLOSE, frame.getDefaultCloseOperation(), "Default close operation should be EXIT_ON_CLOSE");

        JLabel titleLabel = (JLabel) frame.getContentPane().getComponent(0);
        assertNotNull(titleLabel, "Title label should not be null");
        assertEquals("Extracted Entities", titleLabel.getText(), "Title label text should be 'Extracted Entities'");

        JScrollPane scrollPane = (JScrollPane) frame.getContentPane().getComponent(1);
        JList<?> entityList = (JList<?>) scrollPane.getViewport().getView();
        assertNotNull(entityList, "Entity list should not be null");
        ListModel<?> listModel = entityList.getModel();
        Object[] actualEntities = new Object[listModel.getSize()];
        for (int i = 0; i < listModel.getSize(); i++) {
            actualEntities[i] = listModel.getElementAt(i);
        }

        assertArrayEquals(sampleEntities.toArray(), actualEntities, "Entity list content should match sample entities");

        JPanel buttonPanel = (JPanel) frame.getContentPane().getComponent(2);
        JButton saveButton = (JButton) buttonPanel.getComponent(0);
        JButton closeButton = (JButton) buttonPanel.getComponent(1);
        assertNotNull(saveButton, "Save button should not be null");
        assertNotNull(closeButton, "Close button should not be null");
        assertEquals("Save as JSON", saveButton.getText(), "Save button text should be 'Save as JSON'");
        assertEquals("Close", closeButton.getText(), "Close button text should be 'Close'");

        closeButton.doClick();
        assertFalse(frame.isVisible(), "Frame should not be visible after close button click");
    }

    @Test
    public void testSaveEntitiesAsJson() {
        EntityVisualizer visualizer = new EntityVisualizer();

        List<String> sampleEntities = Arrays.asList("Entity1", "Entity2", "Entity3");

        File tempFile = new File("testEntities.json");

        try {
            visualizer.displayEntities(sampleEntities);
            visualizer.saveEntitiesAsJson(sampleEntities, tempFile);

            assertTrue(tempFile.exists(), "JSON file should exist after saving");
            assertTrue(tempFile.length() > 0, "JSON file should not be empty");

            String content = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
            assertTrue(content.contains("Entity1"), "Content should include 'Entity1'");
            assertTrue(content.contains("Entity2"), "Content should include 'Entity2'");
            assertTrue(content.contains("Entity3"), "Content should include 'Entity3'");

        } catch (Exception e) {
            fail("No exception should occur during saveEntitiesAsJson test: " + e.getMessage());
        } finally {
            if (tempFile.exists()) {
                assertTrue(tempFile.delete(), "Temporary file should be deleted after test");
            }
        }
    }
}
