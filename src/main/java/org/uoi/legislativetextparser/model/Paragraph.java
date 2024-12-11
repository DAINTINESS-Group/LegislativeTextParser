package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a paragraph object that contains the paragraph number and text.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Paragraph {

    @JsonProperty("paragraphNumber")
    private int paragraphNumber;

    @JsonProperty("text")
    private String paragraphText;


    public Paragraph(Builder builder) {
        this.paragraphNumber = builder.paragraphNumber;
        this.paragraphText = builder.paragraphText;
    }

    public int getParagraphNumber() {
        return paragraphNumber;
    }

    public void setParagraphNumber(int paragraphNumber) {
        this.paragraphNumber = paragraphNumber;
    }


    public String getParagraphText() {
        return paragraphText;
    }

    public void setParagraphText(String paragraphText) {
        this.paragraphText = paragraphText;
    }

    public static class Builder {

        private int paragraphNumber;
        private String paragraphText;

        public Builder(int paragraphNumber, String paragraphText) {
            this.paragraphNumber = paragraphNumber;
            this.paragraphText = paragraphText;
        }

        public Builder paragraphNumber(int paragraphNumber) {
            this.paragraphNumber = paragraphNumber;
            return this;
        }

        public Builder paragraphText(String paragraphText) {
            this.paragraphText = paragraphText;
            return this;
        }

        public Paragraph build() {
            return new Paragraph(this);
        }
    }
}
