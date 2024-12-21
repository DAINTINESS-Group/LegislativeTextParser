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

    @JsonProperty("paragraphID")
    private String paragraphID;

    @JsonProperty("text")
    private ArrayList<Point> paragraphPoints;


    public Paragraph(Builder builder) {
        this.paragraphNumber = builder.paragraphNumber;
        this.paragraphPoints = builder.paragraphPoints;
        this.paragraphID = builder.paragraphID;
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

    public String getParagraphID() {
        return paragraphID;
    }

    public void setParagraphID(String paragraphID) {
        this.paragraphID = paragraphID;
    }

    public static class Builder {

        private int paragraphNumber;
        private String paragraphID;
        private ArrayList<Point> paragraphPoints;

        public Builder(int paragraphNumber, ArrayList<Point> paragraphPoints, String paragraphID) {
            this.paragraphNumber = paragraphNumber;
            this.paragraphPoints = paragraphPoints;
            this.paragraphID = paragraphID;
        }

        public Builder paragraphNumber(int paragraphNumber) {
            this.paragraphNumber = paragraphNumber;
            return this;
        }

        public Builder paragraphPoints(ArrayList<Point> paragraphPoints) {
            this.paragraphPoints = paragraphPoints;
            return this;
        }

        public Builder paragraphID(String paragraphID) {
            this.paragraphID = paragraphID;
            return this;
        }

        public Paragraph build() {
            return new Paragraph(this);
        }
    }
}
