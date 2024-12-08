package org.uoi.legislativetextparser.textprocessing;


import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for splitting articles into paragraphs.
 */
public class ParagraphSplitter {

    private static final int MEAN_LINE_BREAKS = 9;

    /**
     * Splits the articles into paragraphs after every MEAN_LINE_BREAKS line break or continues
     * until it finds a valid ending ('.' or ';').
     *
     * @param article each article to be split into paragraphs
     * @return a list of paragraphs extracted from the article
     */
    public static List<String> splitIntoParagraphs(String article) {

        List<String> paragraphs = new ArrayList<>();
        int totalLineBreaks = (int) article.chars().filter(ch -> ch == '\n').count();
        if (totalLineBreaks < MEAN_LINE_BREAKS) {
            paragraphs.add(article.trim());
            return paragraphs;
        }

        StringBuilder paragraphBuilder = new StringBuilder();
        int lineBreakCount = 0;

        for (String line : article.split("\\n")) {
            paragraphBuilder.append(line).append("\n");
            if (!line.trim().isEmpty()) {
                lineBreakCount++;
            }
            if (lineBreakCount >= MEAN_LINE_BREAKS &&
                    (paragraphBuilder.toString().trim().endsWith(".") || paragraphBuilder.toString().trim().endsWith(";"))) {

                paragraphs.add(paragraphBuilder.toString().trim());
                paragraphBuilder.setLength(0);
                lineBreakCount = 0;
            }
        }
        String remainingParagraph = paragraphBuilder.toString().trim();
        if (!remainingParagraph.isEmpty()) {
            paragraphs.add(remainingParagraph);
        }
        return paragraphs;
    }


}
