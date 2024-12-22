package org.uoi.legislativetextparser.entityextraction;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts entities from the "Definitions" article of a specific location in the JSON file.
 */
public class SpecificLocationEntityExtractor implements EntityExtractor{

    private final int chapterNumber;
    private final int articleNumber;

    public SpecificLocationEntityExtractor(int chapterNumber, int articleNumber) {
        this.chapterNumber = chapterNumber;
        this.articleNumber = articleNumber;
    }

    @Override
    public List<String> extractEntities(String jsonFilePath) throws Exception {
        List<String> entities = new ArrayList<>();

        String jsonContent = Files.readString(Paths.get(jsonFilePath));
        JSONObject root = new JSONObject(jsonContent);

        JSONArray chapters = root.getJSONArray("chapters");
        for (int i = 0; i < chapters.length(); i++) {
            JSONObject chapter = chapters.getJSONObject(i);
            if (chapter.getInt("chapterNumber") == chapterNumber) {

                JSONArray articles = chapter.getJSONArray("articles");
                for (int j = 0; j < articles.length(); j++) {
                    JSONObject article = articles.getJSONObject(j);
                    if (article.getInt("articleNumber") == this.articleNumber) {

                        JSONArray paragraphs = article.getJSONArray("paragraphs");
                        if (!paragraphs.isEmpty()) {
                            JSONObject firstParagraph = paragraphs.getJSONObject(0);
                            if (EntityExtractor.containsDefinitions(firstParagraph)) {

                                for (int k = 1; k < paragraphs.length(); k++) {
                                    JSONObject paragraph = paragraphs.getJSONObject(k);
                                    JSONArray texts = paragraph.getJSONArray("text");

                                    for (int l = 0; l < texts.length(); l++) {
                                        String paragraphText = texts.getJSONObject(l).getString("text");
                                        String entity = EntityExtractor.extractEntityFromParagraph(paragraphText);
                                        if (entity != null) {
                                            entities.add(entity);
                                        }
                                    }
                                }
                                return entities;
                            }
                        }
                        throw new IllegalArgumentException("The specified article does not contain definitions.");
                    }
                }
                throw new IllegalArgumentException("Article " + this.articleNumber + " not found in chapter " + this.chapterNumber + ".");
            }
        }
        throw new IllegalArgumentException("Chapter " + this.chapterNumber + " not found.");
    }
}
