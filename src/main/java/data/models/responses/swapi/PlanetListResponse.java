package data.models.responses.swapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import data.Constants;

import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetListResponse(
        String message,

        @JsonProperty("total_records")
        int totalRecords,

        @JsonProperty("total_pages")
        int totalPages,

        String next,

        String previous,

        List<PlanetResult> results,

        @JsonProperty("apiVersion")
        String apiVersion,

        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = Constants.ISO_INSTANT_PATTERN,
                timezone = Constants.UTC)
        LocalDateTime timestamp
) {}
