package org.uoi.legislativetextparser.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uoi.legislativetextparser.model.*;
import org.uoi.legislativetextparser.textprocessing.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LawProcessor {

    // Define the path to the legislative document for processing, the output directory for chapters, and the logger instance.
    // private static final String lawPath = "src/main/resources/input/Data_Act.pdf";
    private static final String lawPath = "src/main/resources/input/AI_Law.pdf";
    private static final String CHAPTERS_DIR = "src/main/resources/output/chapters/";
    private static final Logger log = LoggerFactory.getLogger(LawProcessor.class);

    /**
     * Processes the legislative document by extracting text from the PDF, cleaning the text, splitting it into chapters,
     * and calling constructLawObject method to build the Law object.
     *
     * @throws Exception if an error occurs
     */
    public void processLegislativeDocument() {
        long startTime = System.currentTimeMillis();
        log.info("Starting legislative text processing...");

        try {
            // Convert PDF to text
            log.info("Extracting text from the legislative document...");
            PdfToTxtExtractor.extractTextFromPDF(new File(lawPath));
            log.info("Text extraction completed successfully.");
        } catch (Exception e) {
            log.error("Error during PDF to text extraction: {}", e.getMessage(), e);
            return;
        }

        File txtFile = new File("src/main/resources/output/selectedLaw.txt");
        try {
            // Clean text
            log.info("Cleaning text from the legislative document by removing unnecessary sections and characters...");
            TxtCleaner txtCleaner = new TxtCleaner();
            txtCleaner.cleanText(Files.readString(txtFile.toPath()));
            log.info("Text cleaning completed successfully.");
        } catch (IOException e) {
            log.error("I/O Error during text cleaning: {}", e.getMessage(), e);
            return;
        }

        try {
            // Split into chapters
            log.info("Splitting the document into chapters...");
            txtFile = new File("src/main/resources/output/cleanedSelectedLaw.txt");
            ChapterSplitter.splitIntoChapters(Files.readString(txtFile.toPath()));
            log.info("Chapters saved successfully.");
        } catch (IOException e) {
            log.error("I/O Error during chapter splitting: {}", e.getMessage(), e);
            return;
        }

        Law law;
        try {
            // Build the law object
            log.info("Building the law object...");
            law = constructLawObject();
            log.info("Law object built successfully.");
        } catch (NullPointerException | IOException e) {
            log.error("Null pointer encountered during law object construction: {}", e.getMessage(), e);
            return;
        }

        try {
            // Write the law object to a JSON file
            log.info("Writing the law object to JSON file at 'src/main/resources/output/lawObject.json'...");
            Files.writeString(new File("src/main/resources/output/lawObject.json").toPath(), law.toString());
            log.info("Law object written successfully.");
        } catch (IOException e) {
            log.error("I/O Error writing law object to JSON file: {}", e.getMessage(), e);
            return;
        }
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        long milliseconds = (System.currentTimeMillis() - startTime) % 1000;
        log.info("Total time taken: {}s and {}ms.", seconds, milliseconds);
        log.info("Exiting...");

        // System.out.println(law.getChapters().get(0).getArticles().get(0).getParagraphs().get(0).getParagraphPoints().get(0).getPointText());
    }


    /**
     * Constructs a Law object from the text files in the chapters directory.
     *
     * @return the Law object
     * @throws IOException if an I/O error occurs
     */
    private Law constructLawObject() throws IOException {
        File chaptersDir = new File(CHAPTERS_DIR);
        if (!chaptersDir.exists() || !chaptersDir.isDirectory()) {
            throw new IllegalArgumentException("Chapters directory does not exist: " + CHAPTERS_DIR);
        }

        File[] chapterFiles = chaptersDir.listFiles((dir, name) -> name.startsWith("chapter_") && name.endsWith(".txt"));
        Arrays.sort(chapterFiles, (f1, f2) -> {
            int chapterNumber1 = extractChapterNumber(f1.getName());
            int chapterNumber2 = extractChapterNumber(f2.getName());
            return Integer.compare(chapterNumber1, chapterNumber2);
        });
        if (chapterFiles.length == 0) {
            throw new IllegalArgumentException("No chapter files found in directory: " + CHAPTERS_DIR);
        }

        ArrayList<Chapter> chapters = new ArrayList<>();

        int chapterCounter = 1;
        for (File chapterFile : chapterFiles) {
            String chapterText = Files.readString(chapterFile.toPath());

            List<String> articleTexts = ArticleSplitter.splitIntoArticles(chapterText);
            ArrayList<Article> articles = new ArrayList<>();

            int articleCounter = 1;
            for (String articleText : articleTexts) {
                List<String> paragraphTexts = ParagraphSplitter.splitIntoParagraphs(articleText);
                ArrayList<Paragraph> paragraphs = new ArrayList<>();

                int paragraphCounter = 1;
                ArrayList<Point> paragraphPoints;
                for(String paragraphText: paragraphTexts){
                    paragraphPoints = PointSplitter.splitIntoPoints(paragraphText);
                    Paragraph paragraph = new Paragraph.Builder(paragraphCounter++, paragraphPoints).build();
                    paragraphs.add(paragraph);
                }

                Article article = new Article.Builder(articleCounter++, paragraphs).build();
                articles.add(article);
            }
            Chapter chapter = new Chapter.Builder(chapterCounter++, articles).build();
            chapters.add(chapter);
        }
        return new Law.Builder(chapters).build();
    }

    /**
     * Extracts the chapter number from the file name.
     *
     * @param fileName the name of the file
     * @return the chapter number
     */
    private int extractChapterNumber(String fileName) {
        String numberPart = fileName.replace("chapter_", "").replace(".txt", "");
        return Integer.parseInt(numberPart);
    }


}