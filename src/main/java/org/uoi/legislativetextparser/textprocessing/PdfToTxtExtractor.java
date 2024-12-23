package org.uoi.legislativetextparser.textprocessing;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Extracts text from a PDF file and saves it to a text file.
 */
public class PdfToTxtExtractor {

    /**
     * Extracts text from a PDF file.
     *
     * @param pdfFile the PDF file to process
     * @calls saveToFile method to save the extracted text to a file
     * @throws IOException if there is an error reading or processing the PDF file
     */
    public static void extractTextFromPDF(File pdfFile) {
        try (PDDocument document = Loader.loadPDF(pdfFile)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            saveToFile(pdfStripper.getText(document));
        } catch (IOException e) {
            System.err.println("An error occurred while extracting text from the PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Saves the extracted text to a file at ../resources/output/selectedLaw.txt.
     *
     * @param text the extracted text from the PDF
     */
    public static void saveToFile(String text) throws IOException {
        File txtOutputFile = new File("src/main/resources/output/selectedLaw.txt");
        Files.writeString(txtOutputFile.toPath(), text);
    }
}
