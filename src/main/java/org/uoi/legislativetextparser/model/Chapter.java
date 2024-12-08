package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

/**
 * Represents a chapter of a legislative document which consists of a list of Articles.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class Chapter {

    @JsonProperty("chapterNumber")
    private int chapterNumber;

    @JsonProperty("articles")
    private ArrayList<Article> articles;
}
