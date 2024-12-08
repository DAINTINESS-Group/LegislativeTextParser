package org.uoi.legislativetextparser.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;

/**
 * Represents a Law object that contains a list of chapters.
 */
@Data
@Builder
public class Law {

    @JsonProperty("chapters")
    private ArrayList<Chapter> chapters;

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Law to JSON", e);
        }
    }
}

