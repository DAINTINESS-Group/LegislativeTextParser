package org.uoi.legislativetextparser.gui;

import org.json.JSONObject;
import org.uoi.legislativetextparser.tree.LawTreeTextHandler;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;

public class TreeVisualizer {

    /**
     * Displays a hierarchical tree structure along with associated text in a split pane.
     *
     * @param rootNode   The root node of the tree to display.
     * @param jsonObject The JSON object containing the law structure and text.
     * @param relativeTo A component relative to which the window is displayed.
     * @param fullTextPath The file path of the full text to be displayed initially.
     */
    public void displayTree(DefaultMutableTreeNode rootNode, JSONObject jsonObject, Component relativeTo, String fullTextPath) {
        JFrame frame = new JFrame("Law Structure Navigator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setLayout(new BorderLayout());

        JTree tree = new JTree(rootNode);
        JScrollPane treeScrollPane = new JScrollPane(tree);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        String fullText = "";
        try {
            fullText = Files.readString(Paths.get(fullTextPath));
            textArea.setText(fullText);
            textArea.setCaretPosition(0);
        } catch (IOException e) {
            textArea.setText("Failed to load the full text.\n" + e.getMessage());
        }

        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        searchButton.addActionListener(e -> searchTree(tree, searchField.getText()));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane, textScrollPane);
        splitPane.setDividerLocation(400);

        String finalFullText = fullText;
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode != null && selectedNode.getUserObject() instanceof String) {
                String nodeText = selectedNode.getUserObject().toString();

                if (nodeText.equals("Law")){
                    textArea.setText(finalFullText);
                    textArea.setCaretPosition(0);
                }
                else {
                    LawTreeTextHandler textHandler = new LawTreeTextHandler();
                    String associatedText = textHandler.getContentForNode(nodeText, jsonObject);
                    textArea.setText(associatedText);
                    textArea.setCaretPosition(0);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> frame.dispose());
        buttonPanel.add(closeButton);

        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setLocation(relativeTo.getX() + (relativeTo.getWidth()/2), relativeTo.getY());
        frame.setVisible(true);
    }

    /**
     * Searches the tree for nodes matching the query and expands them.
     *
     * @param tree  The JTree to search.
     * @param query The search query.
     */
    private void searchTree(JTree tree, String query) {
        if (query == null || query.isEmpty()) return;

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration<?> enumeration = root.depthFirstEnumeration();

        while (enumeration.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
            if (node.getUserObject().toString().toLowerCase().contains(query.toLowerCase())) {
                TreePath path = new TreePath(node.getPath());
                tree.expandPath(path);
                tree.setSelectionPath(path);
                tree.scrollPathToVisible(path);
                break;
            }
        }
    }
}
