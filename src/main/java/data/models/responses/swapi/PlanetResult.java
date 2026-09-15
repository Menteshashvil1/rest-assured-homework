package data.models.responses.swapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlanetResult(
        String uid,
        String name,
        String url,
        String description,
        PlanetProperties properties
) {}
