package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;

/**
 * Represents a chapter of a legislative document which consists of a list of Articles.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Chapter {

    @JsonProperty("chapterNumber")
    private int chapterNumber;

    @JsonProperty("articles")
    private ArrayList<Article> articles;

    public Chapter(Builder builder) {
        this.chapterNumber = builder.chapterNumber;
        this.articles = builder.articles;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    public static class Builder {

        private int chapterNumber;
        private ArrayList<Article> articles;

        public Builder(int chapterNumber, ArrayList<Article> articles) {
            this.chapterNumber = chapterNumber;
            this.articles = articles;
        }

        public Builder chapterNumber(int chapterNumber) {
            this.chapterNumber = chapterNumber;
            return this;
        }

        public Builder article(ArrayList<Article> articles) {
            this.articles = articles;
            return this;
        }

        public Chapter build() {
            return new Chapter(this);
        }
    }
}
