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

    public static String getTextForChapter(String chapterNodeText, JSONObject jsonObject) {
        JSONArray chapters = jsonObject.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONObject chapter = chapters.getJSONObject(i);
            String chapterTitle = "Chapter " + chapter.getInt("chapterNumber") + ": " + chapter.getString("chapterTitle");
            if (chapterNodeText.trim().equalsIgnoreCase(normalizeTitle(chapterTitle))) {
                StringBuilder chapterText = new StringBuilder();
                chapterText.append(chapter.getString("chapterTitle")).append("\n\n");

                JSONArray articles = chapter.getJSONArray("articles");
                for (int j = 0; j < articles.length(); j++) {
                    JSONObject article = articles.getJSONObject(j);
                    chapterText.append("Article ").append(article.getString("articleID")).append(": ");
                    chapterText.append(article.getString("articleTitle")).append("\n\n");

                    JSONArray paragraphs = article.getJSONArray("paragraphs");
                    for (int k = 0; k < paragraphs.length(); k++) {
                        JSONObject paragraph = paragraphs.getJSONObject(k);
                        chapterText.append("  Paragraph ").append(paragraph.getString("paragraphID")).append("\n");

                        JSONArray points = paragraph.getJSONArray("text");
                        for (int l = 0; l < points.length(); l++) {
                            JSONObject point = points.getJSONObject(l);
                            chapterText.append("    Point ").append(point.getInt("pointNumber")).append(": ");
                            chapterText.append(point.getString("text")).append("\n");
                        }
                        chapterText.append("\n");
                    }
                }
                return chapterText.toString().trim();
            }
        }
        return "Chapter content not found.";
    }

    public static String getTextForArticle(String articleNodeText, JSONObject jsonObject) {
        JSONArray chapters = jsonObject.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONArray articles = chapters.getJSONObject(i).getJSONArray("articles");
            for (int j = 0; j < articles.length(); j++) {
                JSONObject article = articles.getJSONObject(j);
                String articleTitle = "Article " + article.getString("articleID") + ": " + article.getString("articleTitle");
                if (articleNodeText.equalsIgnoreCase(articleTitle)) {
                    StringBuilder articleText = new StringBuilder();
                    articleText.append(article.getString("articleTitle")).append("\n\n");

                    JSONArray paragraphs = article.getJSONArray("paragraphs");
                    for (int k = 0; k < paragraphs.length(); k++) {
                        JSONObject paragraph = paragraphs.getJSONObject(k);
                        articleText.append("Paragraph ").append(paragraph.getString("paragraphID")).append("\n");

                        JSONArray points = paragraph.getJSONArray("text");
                        for (int l = 0; l < points.length(); l++) {
                            JSONObject point = points.getJSONObject(l);
                            articleText.append("  Point ").append(point.getInt("pointNumber")).append(": ");
                            articleText.append(point.getString("text")).append("\n");
                        }
                        articleText.append("\n");
                    }
                    return articleText.toString().trim();
                }
            }
        }
        return "Article content not found.";
    }

    public static String getTextForParagraph(String paragraphNodeText, JSONObject jsonObject) {
        JSONArray chapters = jsonObject.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONArray articles = chapters.getJSONObject(i).getJSONArray("articles");
            for (int j = 0; j < articles.length(); j++) {
                JSONArray paragraphs = articles.getJSONObject(j).getJSONArray("paragraphs");
                for (int k = 0; k < paragraphs.length(); k++) {
                    JSONObject paragraph = paragraphs.getJSONObject(k);
                    if (paragraphNodeText.equals("Paragraph " + paragraph.getString("paragraphID"))) {
                        StringBuilder paragraphText = new StringBuilder();
                        JSONArray points = paragraph.getJSONArray("text");

                        for (int l = 0; l < points.length(); l++) {
                            JSONObject point = points.getJSONObject(l);
                            paragraphText.append("Point ").append(point.getInt("pointNumber")).append(": ");
                            paragraphText.append(point.getString("text")).append("\n");
                        }
                        return paragraphText.toString().trim();
                    }
                }
            }
        }
        return "Paragraph content not found.";
    }


    public static String getTextForPoint(String pointNodeText, JSONObject jsonObject) {
        JSONArray chapters = jsonObject.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONArray articles = chapters.getJSONObject(i).getJSONArray("articles");
            for (int j = 0; j < articles.length(); j++) {
                JSONArray paragraphs = articles.getJSONObject(j).getJSONArray("paragraphs");
                for (int k = 0; k < paragraphs.length(); k++) {
                    JSONArray points = paragraphs.getJSONObject(k).getJSONArray("text");
                    for (int l = 0; l < points.length(); l++) {
                        JSONObject point = points.getJSONObject(l);
                        String pointText = "Point " + point.getInt("pointNumber") + ": " + point.getString("text");
                        if (pointNodeText.equals(pointText)) {
                            return point.getString("text");
                        }
                    }
                }
            }
        }
        return "Point content not found.";
    }


    private static String getArticlesText(JSONArray articles) {
        StringBuilder articlesText = new StringBuilder();
        for (int i = 0; i < articles.length(); i++) {
            JSONObject article = articles.getJSONObject(i);
            articlesText.append("Article ").append(article.getString("articleID")).append(": ").append(article.getString("articleTitle")).append("\\n");
            articlesText.append(getParagraphsText(article.getJSONArray("paragraphs")));
        }
        return articlesText.toString();
    }

    private static String getParagraphsText(JSONArray paragraphs) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < paragraphs.length(); i++) {
            JSONObject paragraph = paragraphs.getJSONObject(i);
            text.append("Paragraph ").append(paragraph.getString("paragraphID")).append("\\n");

            // Traverse points to construct the paragraph text
            JSONArray points = paragraph.getJSONArray("text");
            for (int j = 0; j < points.length(); j++) {
                JSONObject point = points.getJSONObject(j);
                text.append("Point ").append(point.getInt("pointNumber")).append(": ").append(point.getString("text")).append("\\n");
            }

            text.append("\\n");
        }
        return text.toString();
    }

    public static String normalizeTitle(String title) {
        return title.replaceAll("[\n\r]+", " ").replaceAll("\\s+", " ").trim();
    }

}
