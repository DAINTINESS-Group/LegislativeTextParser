package org.uoi.legislativetextparser.engine;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uoi.legislativetextparser.model.Article;
import org.uoi.legislativetextparser.model.Chapter;
import org.uoi.legislativetextparser.model.Law;
import org.uoi.legislativetextparser.model.Paragraph;
import org.uoi.legislativetextparser.textprocessing.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LawProcessor {

    // Define the path to the legislative document for processing, the output directory for chapters, and the logger instance.
    //    private static final String lawPath = "src/main/resources/input/Data_Act.pdf";
    private static final String lawPath = "src/main/resources/input/AI_Law.pdf";
    private static final String CHAPTERS_DIR = "src/main/resources/output/chapters/";
    private static final Logger log = LoggerFactory.getLogger(LawProcessor.class);

    /**
     * Processes the legislative document by extracting text from the PDF, cleaning the text, splitting it into chapters,
     * and calling constructLawObject method to build the Law object.
     *
     * @throws IOException if an I/O error occurs using @SneakyThrows
     */
    @SneakyThrows
    public void processLegislativeDocument() {

        // Convert PDF to text
        long startTime = System.currentTimeMillis();
        log.info("Starting legislative text processing...");
        log.info("Extracting text from the legislative document...");
        PdfToTxtExtractor.extractTextFromPDF(new File(lawPath));
        log.info("Text extraction completed successfully.");

        // Clean text
        log.info("Cleaning text from the legislative document by removing unnecessary sections and characters...");
        File txtFile = new File("src/main/resources/output/selectedLaw.txt");

        TxtCleaner txtCleaner = new TxtCleaner();
        txtCleaner.cleanText(Files.readString(txtFile.toPath()));
        log.info("Text cleaning completed successfully.");

        // Split into chapters
        log.info("Splitting the document into chapters...");
        txtFile = new File("src/main/resources/output/cleanedSelectedLaw.txt");
        ChapterSplitter.splitIntoChapters(Files.readString(txtFile.toPath()));
        log.info("Chapters saved successfully.");

        // Split into articles, then paragraphs, and finally build the law object
        log.info("Building the law object...");
        Law law = constructLawObject();
        log.info("Law object built successfully.");

        // Write the law object to a JSON file
        log.info("Writing the law object to JSON file at 'src/main/resources/output/lawObject.json'...");
        Files.writeString(new File("src/main/resources/output/lawObject.json").toPath(), law.toString());

        // Finish the process and exit
        long seconds = (System.currentTimeMillis() - startTime) / 1000;
        long milliseconds = (System.currentTimeMillis() - startTime) % 1000;
        log.info("Law object written successfully. Total time taken: {}s and {}ms.", seconds, milliseconds);
        log.info("Exiting...");
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
        if (chapterFiles == null || chapterFiles.length == 0) {
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
                for (String paragraphText : paragraphTexts) {
                    paragraphs.add(Paragraph
                            .builder()
                            .paragraphText(paragraphText)
                            .paragraphNumber(paragraphCounter++)
                            .build());
                }
                articles.add(Article
                        .builder()
                        .articleNumber(articleCounter++)
                        .paragraphs(paragraphs)
                        .build());
            }
            chapters.add(Chapter
                    .builder()
                    .chapterNumber(chapterCounter++)
                    .articles(articles)
                    .build());
        }
        return Law.builder().chapters(chapters).build();
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