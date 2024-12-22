package org.uoi.legislativetextparser.engine;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.uoi.legislativetextparser.config.Config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class LawProcessorTest {


    @Test
    public void testProcessLawHappyDayScenario() throws IOException {
        Config config = new Config("src/test/resources/input/AI_Law.pdf", "src/test/resources/output/sample_json.json");
        LawProcessor lawProcessor = new LawProcessor(config);

        lawProcessor.processLegislativeDocument();
        String jsonContent = Files.readString(Paths.get(config.getLawJsonPath()));
        JSONObject root = new JSONObject(jsonContent);

        assertNotNull(root, "Root JSON object should not be null");
        assertTrue(root.has("chapters"), "JSON should have chapters");

        JSONArray chapters = root.getJSONArray("chapters");
        assertEquals(13, chapters.length(), "Law should have correct number of chapters");

        JSONObject firstChapter = chapters.getJSONObject(0);
        assertTrue(firstChapter.has("articles"), "First chapter should have articles");
        JSONArray articles = firstChapter.getJSONArray("articles");
        assertEquals(4, articles.length(), "First chapter should have correct number of articles");

        JSONObject firstArticle = articles.getJSONObject(0);
        assertTrue(firstArticle.has("paragraphs"), "Article should have paragraphs");
        JSONArray paragraphs = firstArticle.getJSONArray("paragraphs");
        assertFalse(paragraphs.isEmpty(), "First article should have at least one paragraph");

        JSONObject firstParagraph = paragraphs.getJSONObject(0);
        assertTrue(firstParagraph.has("text"), "Paragraph should have points");
        assertTrue(firstParagraph.has("paragraphID"), "Paragraph should have paragraphID");
        assertEquals("1.1.1", firstParagraph.getString("paragraphID"), "Paragraph should have correct paragraphID");
    }

    @Test
    public void testProcessLawRainyDayScenarioPdfDoesntExist() {
        Config config = new Config("src/test/resources/input/sample.pdf", "src/test/resources/output/sample_json.json");
        LawProcessor lawProcessor = new LawProcessor(config);

        assertThrows(NoSuchFileException.class, lawProcessor::processLegislativeDocument);
    }
}