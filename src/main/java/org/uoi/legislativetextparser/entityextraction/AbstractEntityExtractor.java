package org.uoi.legislativetextparser.entityextraction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityExtractor implements EntityExtractor{

    /**
     * Reads and parses the JSON file.
     *
     * @param jsonFilePath Path to the JSON file.
     * @return Parsed JSON object.
     * @throws Exception if file reading or parsing fails.
     */
    protected JSONObject parseJsonFile(String jsonFilePath) throws Exception {
        String jsonContent = Files.readString(Paths.get(jsonFilePath));
        return new JSONObject(jsonContent);
    }

    /**
     * Retrieves all chapters from the JSON object.
     *
     * @param root Root JSON object.
     * @return JSONArray of chapters.
     */
    protected JSONArray getChapters(JSONObject root) {
        return root.getJSONArray("chapters");
    }

    /**
     * Retrieves all articles from a chapter.
     *
     * @param chapter Chapter JSON object.
     * @return JSONArray of articles.
     */
    protected JSONArray getArticles(JSONObject chapter) {
        return chapter.getJSONArray("articles");
    }

    /**
     * Retrieves all paragraphs from an article.
     *
     * @param article Article JSON object.
     * @return JSONArray of paragraphs.
     */
    protected JSONArray getParagraphs(JSONObject article) {
        return article.getJSONArray("paragraphs");
    }

    /**
     * Extracts an entity from a paragraph based on a regex pattern.
     *
     * @param paragraph The paragraph text.
     * @return Extracted entity or null if no match is found.
     */
    protected String extractEntityFromParagraph(String paragraph) {
        String entityPattern = "\\(?\\d+[a-z]?\\)?\\.?\\s+‘([^’]+)’";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(entityPattern);
        java.util.regex.Matcher matcher = pattern.matcher(paragraph);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    /**
     * Extracts entities from the given paragraphs.
     *
     * @param paragraphs JSONArray of paragraphs.
     * @return List of extracted entities.
     */
    protected List<String> extractEntitiesFromParagraphs(JSONArray paragraphs) {
        List<String> entities = new ArrayList<>();

        for (int i = 0; i < paragraphs.length(); i++) {
            JSONObject paragraph = paragraphs.getJSONObject(i);
            JSONArray texts = paragraph.getJSONArray("text");

            for (int j = 0; j < texts.length(); j++) {
                String paragraphText = texts.getJSONObject(j).getString("text");
                String entity = extractEntityFromParagraph(paragraphText);
                if (entity != null) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    /**
     * Abstract method to extract entities, implemented by subclasses.
     *
     * @param jsonFilePath Path to the JSON file.
     * @return List of extracted entities.
     * @throws Exception if extraction fails.
     */
    @Override
    public abstract List<String> extractEntities(String jsonFilePath) throws Exception;
}
