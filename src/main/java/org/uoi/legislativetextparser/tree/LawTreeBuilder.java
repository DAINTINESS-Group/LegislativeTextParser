package org.uoi.legislativetextparser.tree;

import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.tree.DefaultMutableTreeNode;

public class LawTreeBuilder {

    public static DefaultMutableTreeNode buildTree(JSONObject json) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Law");
    
        if (json.has("chapters")) {
            JSONArray chapters = json.getJSONArray("chapters");
            addChaptersToTree(chapters, root);
        }
    
        return root;
    }
    
    private static void addChaptersToTree(JSONArray chapters, DefaultMutableTreeNode root) {
        for (int i = 0; i < chapters.length(); i++) {
            JSONObject chapter = chapters.getJSONObject(i);
            String chapterTitle = toTitleCase(chapter.optString("chapterTitle", "Unknown Title"));
            DefaultMutableTreeNode chapterNode = new DefaultMutableTreeNode("Chapter " + chapter.optInt("chapterNumber") + ": " + chapterTitle);
    
            root.add(chapterNode);
    
            if (chapter.has("articles")) {
                JSONArray articles = chapter.getJSONArray("articles");
                addArticlesToTree(articles, chapterNode);
            }
        }
    }
    
    private static void addArticlesToTree(JSONArray articles, DefaultMutableTreeNode chapterNode) {
        for (int j = 0; j < articles.length(); j++) {
            JSONObject article = articles.getJSONObject(j);
            String articleTitle = toTitleCase(article.optString("articleTitle", "Unknown Title"));
            String articleID = article.optString("articleID", "Unknown ID");
            DefaultMutableTreeNode articleNode = new DefaultMutableTreeNode("Article " + articleID + ": " + articleTitle);
    
            chapterNode.add(articleNode);
    
            if (article.has("paragraphs")) {
                JSONArray paragraphs = article.getJSONArray("paragraphs");
                addParagraphsToTree(paragraphs, articleNode);
            }
        }
    }
    
    private static void addParagraphsToTree(JSONArray paragraphs, DefaultMutableTreeNode articleNode) {
        for (int k = 0; k < paragraphs.length(); k++) {
            JSONObject paragraph = paragraphs.getJSONObject(k);
            String paragraphID = paragraph.optString("paragraphID", "Unknown ID");
            DefaultMutableTreeNode paragraphNode = new DefaultMutableTreeNode("Paragraph " + paragraphID);
    
            articleNode.add(paragraphNode);
    
            if (paragraph.has("text")) {
                JSONArray textPoints = paragraph.getJSONArray("text");
                addPointsToTree(textPoints, paragraphNode);
            }
        }
    }
    
    private static void addPointsToTree(JSONArray textPoints, DefaultMutableTreeNode paragraphNode) {
        for (int l = 0; l < textPoints.length(); l++) {
            JSONObject point = textPoints.getJSONObject(l);
            String pointText = "Point " + point.optInt("pointNumber") + ": " + point.optString("text");
            DefaultMutableTreeNode pointNode = new DefaultMutableTreeNode(pointText);
    
            paragraphNode.add(pointNode);
    
            if (point.has("innerPoints")) {
                JSONArray innerPoints = point.getJSONArray("innerPoints");
                addInnerPointsToTree(innerPoints, pointNode);
            }
        }
    }
    
    private static void addInnerPointsToTree(JSONArray innerPoints, DefaultMutableTreeNode pointNode) {
        for (int m = 0; m < innerPoints.length(); m++) {
            JSONObject innerPoint = innerPoints.getJSONObject(m);
            String innerPointText = "Inner Point: " + innerPoint.optString("text");
            DefaultMutableTreeNode innerPointNode = new DefaultMutableTreeNode(innerPointText);
    
            pointNode.add(innerPointNode);
        }
    }

    /**
     * Converts a string to title case.
     *
     * @param input The input string.
     * @return The input string in title case.
     */
    private static String toTitleCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        String[] words = input.toLowerCase().split("\\s+");

        StringBuilder titleCase = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                titleCase.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return titleCase.toString().trim();
    }
}
