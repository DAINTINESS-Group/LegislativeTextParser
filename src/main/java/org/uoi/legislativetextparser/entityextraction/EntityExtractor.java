package org.uoi.legislativetextparser.entityextraction;

import java.util.List;

public interface EntityExtractor {


    /**
    * @param jsonFilePath Path to the JSON file.
    * @return List of extracted entities.
    * @throws Exception if JSON parsing fails or a problem occurs at entity extraction.
    */
    List<String> extractEntities(String jsonFilePath) throws Exception;

}
