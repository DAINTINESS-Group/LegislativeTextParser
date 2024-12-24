package org.uoi.legislativetextparser.gui;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class TreeVisualizerTest {

    @Test
    void testTreeStructure() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Law");
        DefaultMutableTreeNode chapter1 = new DefaultMutableTreeNode("Chapter 1");
        DefaultMutableTreeNode article1 = new DefaultMutableTreeNode("Article 1.1");
        DefaultMutableTreeNode article2 = new DefaultMutableTreeNode("Article 1.2");

        chapter1.add(article1);
        chapter1.add(article2);
        root.add(chapter1);

        assertEquals("Law", root.getUserObject(), "Root node should have the correct label");
        assertEquals(1, root.getChildCount(), "Root node should have one child");
        assertEquals("Chapter 1", ((DefaultMutableTreeNode) root.getChildAt(0)).getUserObject(), "Child node label mismatch");
        assertEquals(2, (root.getChildAt(0)).getChildCount(), "Chapter node should have two articles");
    }

    @Test
    void testTreeVisualizerGUI() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Law");
        DefaultMutableTreeNode chapter1 = new DefaultMutableTreeNode("Chapter 1");
        DefaultMutableTreeNode article1 = new DefaultMutableTreeNode("Article 1.1");
        chapter1.add(article1);
        root.add(chapter1);

        TreeVisualizer visualizer = new TreeVisualizer();

        JFrame mockFrame = new JFrame();
        mockFrame.setSize(200, 200);

        SwingUtilities.invokeLater(() -> {
            visualizer.displayTree(root, mockFrame);

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mockFrame);
            assertNotNull(frame, "Frame should be created");
            assertEquals("Tree Visualizer", frame.getTitle(), "Frame title should match");
            assertEquals(JFrame.DISPOSE_ON_CLOSE, frame.getDefaultCloseOperation(), "Close operation mismatch");

            Component[] components = frame.getContentPane().getComponents();
            assertInstanceOf(JLabel.class, components[0], "First component should be a JLabel");
            assertInstanceOf(JScrollPane.class, components[1], "Second component should be a JScrollPane");
            assertInstanceOf(JButton.class, components[2], "Third component should be a JButton");

            JLabel titleLabel = (JLabel) components[0];
            assertEquals("Tree Visualization", titleLabel.getText(), "Title label text mismatch");

            frame.dispose();
        });
    }
}
