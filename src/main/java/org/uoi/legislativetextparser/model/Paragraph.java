package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Represents a paragraph object that contains the paragraph number and text.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paragraph {

    @JsonProperty("paragraphNumber")
    private int paragraphNumber;

    @JsonProperty("text")
    private ArrayList<Point> paragraphPoints;


    public Paragraph(Builder builder) {
        this.paragraphNumber = builder.paragraphNumber;
        this.paragraphPoints = builder.paragraphPoints;
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }

    public void setParagraphNumber(int paragraphNumber) {
        this.paragraphNumber = paragraphNumber;
    }


    public ArrayList<Point> getParagraphPoints() {
        return paragraphPoints;
    }

    public void setParagraphPoints(ArrayList<Point> paragraphPoints) {
        this.paragraphPoints = paragraphPoints;
    }

    public static class Builder {

        private int paragraphNumber;
        private ArrayList<Point> paragraphPoints;

        public Builder(int paragraphNumber, ArrayList<Point> paragraphPoints) {
            this.paragraphNumber = paragraphNumber;
            this.paragraphPoints = paragraphPoints;
        }

        public Builder paragraphNumber(int paragraphNumber) {
            this.paragraphNumber = paragraphNumber;
            return this;
        }

        public Builder paragraphPoints(ArrayList<Point> paragraphPoints) {
            this.paragraphPoints = paragraphPoints;
            return this;
        }

        public Paragraph build() {
            return new Paragraph(this);
        }
    }
}
