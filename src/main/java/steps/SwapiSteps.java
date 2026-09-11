package steps;

import api.client.SwapiApi;
import data.models.responses.swapi.PlanetDetailResponse;
import data.models.responses.swapi.PlanetListResponse;
import data.models.responses.swapi.PlanetProperties;
import data.models.responses.swapi.PlanetResult;
import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SwapiSteps {

    private static final int OK = 200;

    private final SwapiApi swapiApi = new SwapiApi();

    @Step("Get the planet list")
    public PlanetListResponse getPlanets() {
        return swapiApi.getPlanets()
                .then()
                .statusCode(OK)
                .extract()
                .as(PlanetListResponse.class);
    }

    @Step("Get every planet referenced by results.url")
    public List<PlanetResult> getAllPlanetDetails(List<PlanetResult> results) {
        List<PlanetResult> planets = new ArrayList<>();

        for (PlanetResult result : results) {
            PlanetDetailResponse detail = swapiApi.getPlanetByUrl(result.url())
                    .then()
                    .statusCode(OK)
                    .extract()
                    .as(PlanetDetailResponse.class);

            planets.add(detail.result());
        }

        return planets;
    }

    @Step("Find the {count} most recently created planets")
    public List<PlanetResult> mostRecent(List<PlanetResult> planets, int count) {
        return planets.stream()
                .sorted(Comparator.comparing((PlanetResult planet) -> planet.properties().getCreated()).reversed())
                .limit(count)
                .toList();
    }

    @Step("Find the planet with the longest rotation period")
    public PlanetResult topByRotationPeriod(List<PlanetResult> planets) {
        return planets.stream()
                .max(Comparator.comparingInt(planet -> planet.properties().rotationPeriodAsInt()))
                .orElseThrow();
    }

    public PlanetProperties propertiesOf(PlanetResult planet) {
        return planet.properties();
    }
}
