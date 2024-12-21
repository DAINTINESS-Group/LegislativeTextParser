package org.uoi.legislativetextparser.gui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class TreeVisualizer {

    /**
     * Displays a hierarchical tree structure in a Swing GUI.
     *
     * @param rootNode The root node of the tree to display.
     */
    public void displayTree(DefaultMutableTreeNode rootNode, Component relativeTo) {
        JFrame frame = new JFrame("Law Structure Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);
        frame.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Law Structure", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        frame.add(titleLabel, BorderLayout.NORTH);

        JTree tree = new JTree(rootNode);
        JScrollPane scrollPane = new JScrollPane(tree);
        frame.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> frame.dispose());
        frame.add(closeButton, BorderLayout.SOUTH);

        if (relativeTo != null) {
            frame.setLocationRelativeTo(relativeTo);
            frame.setLocation(relativeTo.getX() + 400, relativeTo.getY());
        } else {
            frame.setLocationRelativeTo(null);
        }

        frame.setVisible(true);
    }

}
