package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;

/**
 * Represents a Law object that contains a list of chapters.
 */

public class Law {

    @JsonProperty("chapters")
    private ArrayList<Chapter> chapters;


    public Law(Builder builder) {
        this.chapters = builder.chapters;
    }

    public ArrayList<Chapter> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Chapter> chapters) {
        this.chapters = chapters;
    }



    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Law to JSON", e);
        }
    }

    public static class Builder {
        private ArrayList<Chapter> chapters;

        public Builder(ArrayList<Chapter> chapters) {
            this.chapters = chapters;
        }

        public Builder chapter(ArrayList<Chapter> chapters) {
            this.chapters = chapters;
            return this;
        }

        public Law build() {
            return new Law(this);
        }
    }
}

