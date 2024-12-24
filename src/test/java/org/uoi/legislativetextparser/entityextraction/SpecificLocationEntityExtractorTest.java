package org.uoi.legislativetextparser.entityextraction;

import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SpecificLocationEntityExtractorTest {

    private SpecificLocationEntityExtractor extractor;
    private static final String SAMPLE_JSON_FILE_PATH = "src/test/resources/input/sample.json";
    private static final String EMPTY_JSON_FILE_PATH = "src/test/resources/input/empty.json";

    @BeforeEach
    public void setUp() {
        extractor = new SpecificLocationEntityExtractor(1, 1);
    }

    @Test
    public void testExtractEntities() throws Exception {
        List<String> entities = extractor.extractEntities( SAMPLE_JSON_FILE_PATH);

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals("Entity A", entities.get(0));
    }

    @Test
    public void testInvalidChapterOrArticle() {
        assertThrows(JSONException.class, () -> {
            extractor = new SpecificLocationEntityExtractor(99, 99);
            extractor.extractEntities(EMPTY_JSON_FILE_PATH);
        });
    }
}
