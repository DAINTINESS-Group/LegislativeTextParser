package org.uoi.legislativetextparser.gui;

import org.uoi.legislativetextparser.model.Node;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class TreeVisualizer {

    /**
     * Displays a hierarchical tree structure along with associated text in a split pane.
     *
     * @param rootNode   The root node of the tree to display.
     * @param relativeTo A component relative to which the window is displayed.
     */
    public void displayTree(DefaultMutableTreeNode rootNode, Component relativeTo) {
        JFrame frame = new JFrame("Law Structure Navigator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLayout(new BorderLayout());

        JTree tree = new JTree(rootNode);
        JScrollPane treeScrollPane = new JScrollPane(tree);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        if (rootNode.getUserObject() instanceof Node) {
            Node root = (Node) rootNode.getUserObject();
            textArea.setText(root.getText());
            textArea.setCaretPosition(0);
        }

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, textScrollPane);
        splitPane.setDividerLocation(400);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getUserObject() instanceof Node) {
                Node node = (Node) selectedNode.getUserObject();
                textArea.setText(node.getText());
                textArea.setCaretPosition(0);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeButton);

        frame.add(splitPane, BorderLayout.CENTER);
        frame.setLocationRelativeTo(relativeTo);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}