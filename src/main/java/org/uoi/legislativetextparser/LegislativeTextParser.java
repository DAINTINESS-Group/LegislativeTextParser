package org.uoi.legislativetextparser;

import org.uoi.legislativetextparser.engine.LawProcessor;
import org.uoi.legislativetextparser.gui.EntityVisualizer;
import org.uoi.legislativetextparser.textprocessing.MainEntityExtractor;

import java.util.List;

/**
 * Main class to start the application.
 */
public class LegislativeTextParser {

    public static void main(String[] args) {
        LawProcessor lawProcessor = new LawProcessor();
        lawProcessor.processLegislativeDocument();
        MainEntityExtractor extractor = new MainEntityExtractor();
        EntityVisualizer visualizer = new EntityVisualizer();
        try {
            String jsonFilePath = "src/main/resources/output/lawObject.json";

            List<String> entities = extractor.extractEntities(jsonFilePath);

            visualizer.displayEntities(entities);

            System.out.println("\nTotal Entities: " + entities.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}