package org.uoi.legislativetextparser.textprocessing;

import org.uoi.legislativetextparser.model.Point;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PointSplitter {

    /**
     * Splits the paragraph into points and handles nested subpoints.
     *
     * @param paragraph The paragraph to be split into points.
     * @return A list of Point objects representing the structure.
     */
    public static ArrayList<Point> splitIntoPoints(String paragraph) {
        ArrayList<Point> points = new ArrayList<>();

        // Reset the point counter to 1 for each paragraph
        int pointCounter = 1;

        // Regex for top-level points (e.g., (a), (b), etc.)
        Pattern topLevelPattern = Pattern.compile("(?<=\\n)\\([a-z]\\)\\s");
        Matcher topLevelMatcher = topLevelPattern.matcher(paragraph);

        int start = 0;
        boolean hasDepth = false;
        while (topLevelMatcher.find()) {
            int end = topLevelMatcher.start();

            // Extract text for the current top-level point
            String pointText = paragraph.substring(start, end).trim();
            if (!pointText.isEmpty()) {
                // Reset the counter for subpoints
                int innerPointCounter = 1;

                // Extract subpoints (e.g., (i), (ii)) for this top-level point
                ArrayList<Point> subPoints = extractSubPoints(pointText, innerPointCounter);

                hasDepth = subPoints.size() > 1;

                // Add the top-level point with subpoints directly nested
                points.add( hasDepth ? new Point.Builder(pointCounter++, pointText)
                        .innerPoints(subPoints)
                        .build()
                        : new Point.Builder(pointCounter++, pointText)
                        .build());
            }

            start = end;
        }

        // Handle remaining text as the last point
        String remainingText = paragraph.substring(start).trim();
        if (!remainingText.isEmpty()) {
            points.add(new Point.Builder(pointCounter++, remainingText).build());
        }

        return points;
    }

    /**
     * Extracts subpoints (e.g., (i), (ii), etc.) from the given text and ensures they are nested under their parent.
     *
     * @param text The text to extract subpoints from.
     * @param innerPointCounter The counter for numbering the subpoints.
     * @return A list of Point objects for the subpoints.
     */
    private static ArrayList<Point> extractSubPoints(String text, int innerPointCounter) {
        ArrayList<Point> subPoints = new ArrayList<>();

        // Regex for subpoints (e.g., (i), (ii), etc.)
        Pattern subPointPattern = Pattern.compile("(?<=\\n)\\(([ivxlc]+)\\)\\s");
        Matcher subPointMatcher = subPointPattern.matcher(text);

        int start = 0;
        while (subPointMatcher.find()) {
            int end = subPointMatcher.start();

            // Extract text for the current subpoint
            String subPointText = text.substring(start, end).trim();
            if (!subPointText.isEmpty()) {
                subPoints.add(new Point.Builder(innerPointCounter++, subPointText).build());
            }

            start = end;
        }

        // Handle remaining text as the last subpoint
        String remainingText = text.substring(start).trim();
        if (!remainingText.isEmpty()) {
            subPoints.add(new Point.Builder(innerPointCounter++, remainingText).build());
        }

        return subPoints;
    }
}
