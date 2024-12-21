package org.uoi.legislativetextparser.entityextraction;

import java.util.List;

public interface EntityExtractor {
    /**
     * @param jsonFilePath Path to the JSON file.
     * @return List of extracted entities.
     * @throws Exception if JSON parsing fails or a problem occurs at entity extraction.
     */
    List<String> extractEntities(String jsonFilePath) throws Exception;


    /**
     * Extracts the entity from a paragraph.
     *
     * @param paragraph Text of the paragraph.
     * @return The extracted entity or null if none found.
     */
    static String extractEntityFromParagraph(String paragraph) {

        String entityPattern = "\\(?\\d+[a-z]?\\)?\\.?\\s+‘([^’]+)’";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(entityPattern);
        java.util.regex.Matcher matcher = pattern.matcher(paragraph);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
