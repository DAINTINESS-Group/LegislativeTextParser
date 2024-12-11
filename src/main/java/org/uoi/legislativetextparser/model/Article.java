package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * Represents an article of a legislative document which consists of a list of paragraphs.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {

    @JsonProperty("articleNumber")
    private int articleNumber;

    @JsonProperty("paragraphs")
    private ArrayList<Paragraph> paragraphs;


    public Article(Builder builder) {
        this.articleNumber = builder.articleNumber;
        this.paragraphs = builder.paragraphs;
    }

    public int getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(int articleNumber) {
        this.articleNumber = articleNumber;
    }

    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(ArrayList<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public static class Builder {

        private int articleNumber;
        private ArrayList<Paragraph> paragraphs;

        public Builder(int articleNumber, ArrayList<Paragraph> paragraphs) {
            this.articleNumber = articleNumber;
            this.paragraphs = paragraphs;
        }

        public Builder articleNumber(int articleNumber) {
            this.articleNumber = articleNumber;
            return this;
        }

        public Builder paragraph(ArrayList<Paragraph> paragraph) {
            this.paragraphs = paragraph;
            return this;
        }

        public Article build() {
            return new Article(this);
        }
    }
}

