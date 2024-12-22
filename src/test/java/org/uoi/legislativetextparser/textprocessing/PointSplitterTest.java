package org.uoi.legislativetextparser.textprocessing;

import org.junit.jupiter.api.Test;
import org.uoi.legislativetextparser.model.Point;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PointSplitterTest {
    @Test
    void testSplitIntoPointsSimpleParagraph() {
        String paragraph = "(a) First point text.\n(b) Second point text.\n(c) Third point text.";

        ArrayList<Point> points = PointSplitter.splitIntoPoints(paragraph);

        assertNotNull(points, "Points list should not be null");
        assertEquals(3, points.size(), "There should be three top-level points");
        assertEquals("(a) First point text.", points.get(0).getPointText(), "First point text is incorrect");
        assertEquals("(b) Second point text.", points.get(1).getPointText(), "Second point text is incorrect");
        assertEquals("(c) Third point text.", points.get(2).getPointText(), "Third point text is incorrect");
    }

    @Test
    void testSplitIntoPointsWithSubpoints() {
        String paragraph = "(a) Main point text.\n(i) Subpoint one text.\n(ii) Subpoint two text.";

        ArrayList<Point> points = PointSplitter.splitIntoPoints(paragraph);

        assertNotNull(points, "Points list should not be null");
        assertEquals(1, points.size(), "There should be one top-level point");

        Point mainPoint = points.get(0);
        assertEquals("(a) Main point text.\n(i) Subpoint one text.\n(ii) Subpoint two text.", mainPoint.getPointText(), "Main point text is incorrect");

        ArrayList<Point> subPoints = mainPoint.getInnerPoints();
        assertNotNull(subPoints, "Subpoints list should not be null");
        assertEquals(2, subPoints.size(), "There should be two subpoints");
        assertEquals("(i) Subpoint one text.", subPoints.get(0).getPointText(), "First subpoint text is incorrect");
        assertEquals("(ii) Subpoint two text.", subPoints.get(1).getPointText(), "Second subpoint text is incorrect");
    }

    @Test
    void testSplitIntoPointsEmptyParagraph() {
        String paragraph = "";

        ArrayList<Point> points = PointSplitter.splitIntoPoints(paragraph);

        assertNotNull(points, "Points list should not be null");
        assertTrue(points.isEmpty(), "Points list should be empty for an empty paragraph");
    }

    @Test
    void testExtractSubPointsSimpleSubpoints() {
        String text = "(i) Subpoint one text.\n(ii) Subpoint two text.\n(iii) Subpoint three text.";

        ArrayList<Point> subPoints = PointSplitter.extractSubPoints(text);

        assertNotNull(subPoints, "Subpoints list should not be null");
        assertEquals(3, subPoints.size(), "There should be three subpoints");
        assertEquals("(i) Subpoint one text.", subPoints.get(0).getPointText(), "First subpoint text is incorrect");
        assertEquals("(ii) Subpoint two text.", subPoints.get(1).getPointText(), "Second subpoint text is incorrect");
        assertEquals("(iii) Subpoint three text.", subPoints.get(2).getPointText(), "Third subpoint text is incorrect");
    }

    @Test
    void testExtractSubPointsHandlesTrailingText() {
        String text = "(i) Subpoint one text.\n(ii) Subpoint two text.\nThis is trailing text.";

        ArrayList<Point> subPoints = PointSplitter.extractSubPoints(text);

        assertNotNull(subPoints, "Subpoints list should not be null");
        assertEquals(3, subPoints.size(), "There should be two subpoints and one remaining text");

        assertEquals("(i) Subpoint one text.", subPoints.get(0).getPointText(), "First subpoint text is incorrect");
        assertEquals("(ii) Subpoint two text.", subPoints.get(1).getPointText(), "Second subpoint text is incorrect");
        assertEquals("This is trailing text.", subPoints.get(2).getPointText(), "Remaining text is incorrect");
    }

    @Test
    void testSplitIntoPointsComplexStructure() {
        String paragraph = "(a) Main point text.\n(i) Subpoint one text.\n(b) Second main point.";

        ArrayList<Point> points = PointSplitter.splitIntoPoints(paragraph);

        assertNotNull(points, "Points list should not be null");
        assertEquals(2, points.size(), "There should be two top-level points");

        Point firstPoint = points.get(0);
        assertEquals("(a) Main point text.", firstPoint.getPointText(), "First point text is incorrect");

        ArrayList<Point> firstSubPoints = firstPoint.getInnerPoints();
        assertNotNull(firstSubPoints, "First point subpoints should not be null");
        assertEquals(1, firstSubPoints.size(), "First point should have one subpoint");
        assertEquals("(i) Subpoint one text.", firstSubPoints.get(0).getPointText(), "First subpoint text is incorrect");

        Point secondPoint = points.get(1);
        assertEquals("(b) Second main point.", secondPoint.getPointText(), "Second point text is incorrect");
        assertTrue(secondPoint.getInnerPoints().isEmpty(), "Second point should not have subpoints");
    }


}
