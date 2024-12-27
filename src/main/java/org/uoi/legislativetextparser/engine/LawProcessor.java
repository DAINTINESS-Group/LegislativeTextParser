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

    private static final Logger log = LoggerFactory.getLogger(LawProcessor.class);
    private final Config config;
    private final LawConstructor lawConstructor;

    public LawProcessor(Config config) {
        this.config = config;
        this.lawConstructor = new LawConstructor(Config.getChaptersDir());
    }

    public void processLegislativeDocument() {
        long startTime = System.currentTimeMillis();
        log.info("Starting legislative text processing...");
    
        if (!extractTextFromPDF()) return;
        if (!cleanText()) return;
        if (!splitIntoChapters()) return;
    
        Law law = buildLawObject();
        if (law == null) return;
    
        if (!writeLawToJSON(law)) return;
    
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        long milliseconds = (System.currentTimeMillis() - startTime) % 1000;
        log.info("Total time taken: {}s and {}ms.", seconds, milliseconds);
    }

    private boolean extractTextFromPDF() {
        try {
            log.info("Extracting text from the legislative document...");
            PdfToTxtExtractor pdfToTxtExtractor = new PdfToTxtExtractor(Config.getSelectedLaw());
            pdfToTxtExtractor.extractTextFromPDF(new File(config.getLawPdfPath()));
            log.info("Text extraction completed successfully.");
            return true;
        } catch (Exception e) {
            log.error("Error during PDF to text extraction: {}", e.getMessage(), e);
            return false;
        }
    }

    private boolean cleanText() {
        File txtFile = new File(Config.getSelectedLaw());
        try {
            log.info("Cleaning text from the legislative document...");
            TxtCleaner txtCleaner = new TxtCleaner();
            txtCleaner.cleanText(Files.readString(txtFile.toPath()));
            log.info("Text cleaning completed successfully.");
            return true;
        } catch (IOException e) {
            log.error("I/O Error during text cleaning: {}", e.getMessage(), e);
            return false;
        }
    }

    private boolean splitIntoChapters() {
        File txtFile = new File(Config.getCleanedLaw());
        try {
            log.info("Splitting the document into chapters...");
            ChapterSplitter chapterSplitter = new ChapterSplitter(Config.getChaptersDir());
            chapterSplitter.splitIntoChapters(Files.readString(txtFile.toPath()));
            log.info("Chapters saved successfully.");
            return true;
        } catch (IOException e) {
            log.error("I/O Error during chapter splitting: {}", e.getMessage(), e);
            return false;
        }
    }

    private Law buildLawObject() {
        try {
            log.info("Building the law object...");
            Law law = lawConstructor.constructLawObject();
            log.info("Law object built successfully.");
            return law;
        } catch (NullPointerException | IOException e) {
            log.error("Error during law object construction: {}", e.getMessage(), e);
            return null;
        }
    }

    
    private boolean writeLawToJSON(Law law) {
        try {
            log.info("Writing the law object to JSON file...");
            Files.writeString(new File(config.getLawJsonPath()).toPath(), law.toString());
            log.info("Law object written successfully.");
            return true;
        } catch (IOException e) {
            log.error("I/O Error writing law object to JSON file: {}", e.getMessage(), e);
            return false;
        }
    }
    
    
}
