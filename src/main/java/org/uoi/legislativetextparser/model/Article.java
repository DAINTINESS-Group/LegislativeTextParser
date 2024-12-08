package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;

/**
 * Represents an article of a legislative document which consists of a list of paragraphs.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class Article {

    @JsonProperty("articleNumber")
    private int articleNumber;

    @JsonProperty("paragraphs")
    private ArrayList<Paragraph> paragraphs;
}

