package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a paragraph object that contains the paragraph number and text.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class Paragraph {

    @JsonProperty("paragraphNumber")
    private int paragraphNumber;

    @JsonProperty("text")
    private String paragraphText;
}
