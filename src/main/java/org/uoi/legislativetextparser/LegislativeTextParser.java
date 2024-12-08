package org.uoi.legislativetextparser;

import org.uoi.legislativetextparser.engine.LawProcessor;

/**
 * Main class to start the application.
 */
public class LegislativeTextParser {

    public static void main(String[] args) {
        LawProcessor lawProcessor = new LawProcessor();
        lawProcessor.processLegislativeDocument();
    }
}