package data.models.responses.swapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import data.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlanetProperties {

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = Constants.ISO_INSTANT_PATTERN,
            timezone = Constants.UTC)
    private LocalDateTime created;

    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = Constants.ISO_INSTANT_PATTERN,
            timezone = Constants.UTC)
    private LocalDateTime edited;

    private String name;
    private String climate;
    private String terrain;
    private String gravity;
    private String diameter;
    private String population;
    private String url;

    @JsonProperty("rotation_period")
    private String rotationPeriod;

    @JsonProperty("orbital_period")
    private String orbitalPeriod;

    @JsonProperty("surface_water")
    private String surfaceWater;

    public int rotationPeriodAsInt() {
        return Constants.UNKNOWN.equalsIgnoreCase(rotationPeriod)
                ? Integer.MIN_VALUE
                : Integer.parseInt(rotationPeriod);
    }
}
