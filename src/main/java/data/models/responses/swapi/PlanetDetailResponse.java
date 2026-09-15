package data.models.responses.swapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import data.Constants;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetDetailResponse(
        String message,

        PlanetResult result,

        @JsonFormat(shape = JsonFormat.Shape.STRING,
                pattern = Constants.ISO_INSTANT_PATTERN,
                timezone = Constants.UTC)
        LocalDateTime timestamp
) {}
