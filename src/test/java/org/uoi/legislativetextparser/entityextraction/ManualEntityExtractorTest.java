package org.uoi.legislativetextparser.entityextraction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ManualEntityExtractorTest {

    private ManualEntityExtractor extractor;
    private String sampleJsonFilePath;

    @BeforeEach
    public void setUp() {
        extractor = new ManualEntityExtractor();
        sampleJsonFilePath = "src/test/resources/input/sample.json";
    }

    @Test
    public void testExtractEntities() throws Exception {
        List<String> entities = extractor.extractEntities(sampleJsonFilePath);

        assertNotNull(entities);
        assertFalse(entities.isEmpty());
        assertTrue(entities.contains("Entity A"));
    }

    @Test
    public void testEmptyJsonFile() {
        assertThrows(Exception.class, () -> extractor.extractEntities("src/test/resources/input/empty.json"));
    }
}
