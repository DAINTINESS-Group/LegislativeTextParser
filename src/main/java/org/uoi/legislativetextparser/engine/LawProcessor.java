package org.uoi.legislativetextparser.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uoi.legislativetextparser.config.Config;
import org.uoi.legislativetextparser.model.Law;
import org.uoi.legislativetextparser.textprocessing.ChapterSplitter;
import org.uoi.legislativetextparser.textprocessing.PdfToTxtExtractor;
import org.uoi.legislativetextparser.textprocessing.TxtCleaner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class LawProcessor {

    private static final String CHAPTERS_DIR = "src/main/resources/output/chapters/";
    private static final String SELECTED_LAW = "src/main/resources/output/selectedLaw.txt";
    private static final String CLEANED_LAW = "src/main/resources/output/cleanedSelectedLaw.txt";
    private static final Logger log = LoggerFactory.getLogger(LawProcessor.class);
    private final Config config;
    private final LawConstructor lawConstructor;

    public LawProcessor(Config config) {
        this.config = config;
        this.lawConstructor = new LawConstructor(CHAPTERS_DIR);
    }

    public void processLegislativeDocument() {
        long startTime = System.currentTimeMillis();
        log.info("Starting legislative text processing...");

        try {
            log.info("Extracting text from the legislative document...");
            PdfToTxtExtractor pdfToTxtExtractor = new PdfToTxtExtractor(SELECTED_LAW);
            pdfToTxtExtractor.extractTextFromPDF(new File(config.getLawPdfPath()));
            log.info("Text extraction completed successfully.");
        } catch (Exception e) {
            log.error("Error during PDF to text extraction: {}", e.getMessage(), e);
            return;
        }

        File txtFile = new File(SELECTED_LAW);
        try {
            log.info("Cleaning text from the legislative document...");
            TxtCleaner txtCleaner = new TxtCleaner();
            txtCleaner.cleanText(Files.readString(txtFile.toPath()));
            log.info("Text cleaning completed successfully.");
        } catch (IOException e) {
            log.error("I/O Error during text cleaning: {}", e.getMessage(), e);
            return;
        }

        try {
            log.info("Splitting the document into chapters...");
            txtFile = new File(CLEANED_LAW);
            ChapterSplitter chapterSplitter = new ChapterSplitter(CHAPTERS_DIR);
            chapterSplitter.splitIntoChapters(Files.readString(txtFile.toPath()));
            log.info("Chapters saved successfully.");
        } catch (IOException e) {
            log.error("I/O Error during chapter splitting: {}", e.getMessage(), e);
            return;
        }

        Law law;
        try {
            log.info("Building the law object...");
            law = lawConstructor.constructLawObject();
            log.info("Law object built successfully.");
        } catch (NullPointerException | IOException e) {
            log.error("Error during law object construction: {}", e.getMessage(), e);
            return;
        }

        try {
            log.info("Writing the law object to JSON file...");
            Files.writeString(new File(config.getLawJsonPath()).toPath(), law.toString());
            log.info("Law object written successfully.");
        } catch (IOException e) {
            log.error("I/O Error writing law object to JSON file: {}", e.getMessage(), e);
            return;
        }

        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        long milliseconds = (System.currentTimeMillis() - startTime) % 1000;
        log.info("Total time taken: {}s and {}ms.", seconds, milliseconds);
    }
}
