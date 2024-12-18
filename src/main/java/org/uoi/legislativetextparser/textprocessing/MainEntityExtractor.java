package org.uoi.legislativetextparser.textprocessing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainEntityExtractor {

    /**
     * Extracts main entities from all "Definitions" articles in the JSON file.
     *
     * @param jsonFilePath Path to the JSON file.
     * @return List of main entities.
     * @throws Exception if file reading or JSON parsing fails.
     */
    public List<String> extractEntities(String jsonFilePath) throws Exception {
        List<String> entities = new ArrayList<>();

        String jsonContent = Files.readString(Paths.get(jsonFilePath));
        JSONObject root = new JSONObject(jsonContent);

        JSONArray chapters = root.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONObject chapter = chapters.getJSONObject(i);

            JSONArray articles = chapter.getJSONArray("articles");
            for (int j = 0; j < articles.length(); j++) {
                JSONObject article = articles.getJSONObject(j);
                JSONArray paragraphs = article.getJSONArray("paragraphs");

                if (paragraphs.length() > 0) {
                    JSONObject firstParagraph = paragraphs.getJSONObject(0);
                    if (containsDefinitions(firstParagraph)) {

                        for (int k = 1; k < paragraphs.length(); k++) {
                            JSONObject paragraph = paragraphs.getJSONObject(k);
                            JSONArray texts = paragraph.getJSONArray("text");

                            for (int l = 0; l < texts.length(); l++) {
                                String paragraphText = texts.getJSONObject(l).getString("text");
                                String entity = extractEntityFromParagraph(paragraphText);
                                if (entity != null) {
                                    entities.add(entity);
                                }
                            }
                        }
                    }
                }
            }
        }

        return entities;
    }

    /**
     * Checks if a paragraph contains "Definitions" in its title.
     *
     * @param paragraph The JSON object of the paragraph.
     * @return True if the paragraph contains "Definitions," otherwise false.
     */
    private static boolean containsDefinitions(JSONObject paragraph) {
        JSONArray textArray = paragraph.getJSONArray("text");

        if (textArray.length() > 0) {
            String text = textArray.getJSONObject(0).getString("text");
            return text.contains("Definitions");
        }
        return false;
    }

    /**
     * Extracts the entity from a paragraph.
     *
     * @param paragraph Text of the paragraph.
     * @return The extracted entity or null if none found.
     */
    private static String extractEntityFromParagraph(String paragraph) {

        String entityPattern = "\\(?\\d+[a-z]?\\)?\\.?\\s+‘([^’]+)’";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(entityPattern);
        java.util.regex.Matcher matcher = pattern.matcher(paragraph);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
