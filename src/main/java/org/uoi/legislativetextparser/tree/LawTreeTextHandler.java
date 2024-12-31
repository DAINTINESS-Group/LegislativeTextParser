package org.uoi.legislativetextparser.tree;

import org.json.JSONArray;
import org.json.JSONObject;

public class LawTreeTextHandler implements NodeContentProvider {

    @Override
    public String getContentForNode(String nodeText, JSONObject jsonObject) {
        if (nodeText.startsWith("Chapter")) {
            return getTextForChapter(nodeText, jsonObject);
        } else if (nodeText.startsWith("Article")) {
            return getTextForArticle(nodeText, jsonObject);
        } else if (nodeText.startsWith("Paragraph")) {
            return getTextForParagraph(nodeText, jsonObject);
        } else if (nodeText.startsWith("Point")) {
            return getTextForPoint(nodeText, jsonObject);
        }
        return "Content not found.";
    }


    private String formatPoint(JSONObject point) {
        return "Point " + point.getInt("pointNumber") + ": " + point.getString("text") + "\n";
    }

    private String formatParagraph(JSONObject paragraph) {
        StringBuilder paragraphText = new StringBuilder();
        paragraphText.append("Paragraph ").append(paragraph.getString("paragraphID")).append("\n");

        JSONArray points = paragraph.getJSONArray("text");
        for (int l = 0; l < points.length(); l++) {
            JSONObject point = points.getJSONObject(l);
            paragraphText.append("  ").append(formatPoint(point));
        }

        return paragraphText.toString();
    }

    private String formatArticle(JSONObject article) {
        StringBuilder articleText = new StringBuilder();
        articleText.append(article.getString("articleTitle")).append("\n\n");

        JSONArray paragraphs = article.getJSONArray("paragraphs");
        for (int k = 0; k < paragraphs.length(); k++) {
            JSONObject paragraph = paragraphs.getJSONObject(k);
            articleText.append(formatParagraph(paragraph)).append("\n");
        }

        return articleText.toString();
    }

    private String formatChapter(JSONObject chapter) {
        StringBuilder chapterText = new StringBuilder();
        chapterText.append(chapter.getString("chapterTitle")).append("\n\n");

        JSONArray articles = chapter.getJSONArray("articles");
        for (int j = 0; j < articles.length(); j++) {
            JSONObject article = articles.getJSONObject(j);
            chapterText.append("Article ").append(article.getString("articleID")).append(": ");
            chapterText.append(formatArticle(article)).append("\n");
        }

        return chapterText.toString();
    }

    public String getTextForChapter(String chapterNodeText, JSONObject jsonObject) {
        JSONArray chapters = jsonObject.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONObject chapter = chapters.getJSONObject(i);
            String normalizedTitle = normalizeTitle("Chapter " + chapter.getInt("chapterNumber") + ": " + chapter.getString("chapterTitle"));
            if (chapterNodeText.trim().equalsIgnoreCase(normalizedTitle)) {
                return formatChapter(chapter).trim();
            }
        }
        return "Chapter content not found.";
    }

    public String getTextForArticle(String articleNodeText, JSONObject jsonObject) {
        JSONArray chapters = jsonObject.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONArray articles = chapters.getJSONObject(i).getJSONArray("articles");
            for (int j = 0; j < articles.length(); j++) {
                JSONObject article = articles.getJSONObject(j);
                String articleTitle = "Article " + article.getString("articleID") + ": " + article.getString("articleTitle");
                if (articleNodeText.equalsIgnoreCase(articleTitle)) {
                    return formatArticle(article).trim();
                }
            }
        }
        return "Article content not found.";
    }

    public String getTextForParagraph(String paragraphNodeText, JSONObject jsonObject) {
        JSONArray chapters = jsonObject.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONArray articles = chapters.getJSONObject(i).getJSONArray("articles");
            for (int j = 0; j < articles.length(); j++) {
                JSONArray paragraphs = articles.getJSONObject(j).getJSONArray("paragraphs");
                for (int k = 0; k < paragraphs.length(); k++) {
                    JSONObject paragraph = paragraphs.getJSONObject(k);
                    if (paragraphNodeText.equals("Paragraph " + paragraph.getString("paragraphID"))) {
                        return formatParagraph(paragraph).trim();
                    }
                }
            }
        }
        return "Paragraph content not found.";
    }

    public String getTextForPoint(String pointNodeText, JSONObject jsonObject) {
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

    public String normalizeTitle(String title) {
        return title.replaceAll("[\n\r]+", " ").replaceAll("\\s+", " ").trim();
    }
}
