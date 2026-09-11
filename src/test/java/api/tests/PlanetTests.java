package api.tests;

import data.models.responses.swapi.PlanetListResponse;
import data.models.responses.swapi.PlanetProperties;
import data.models.responses.swapi.PlanetResult;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import steps.SwapiSteps;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

@Epic("Advanced object mapping")
@Feature("SWAPI planets")
public class PlanetTests {

    private static final int MOST_RECENT_COUNT = 3;
    private static final String TOP_ROTATION_PLANET = "Kamino";

    private final SwapiSteps swapiSteps = new SwapiSteps();

    private PlanetListResponse planetList;
    private List<PlanetResult> planets;

    @BeforeClass(alwaysRun = true)
    public void fetchPlanets() {
        planetList = swapiSteps.getPlanets();
        planets = swapiSteps.getAllPlanetDetails(planetList.results());
    }

    @Test
    @Description("Five field validations on the deserialized planet list record")
    public void planetListRecordIsFullyPopulated() {
        assertThat(planetList.message(), equalTo("ok"));
        assertThat(planetList.totalRecords(), greaterThan(0));
        assertThat(planetList.totalPages(), greaterThan(0));
        assertThat(planetList.results(), not(hasSize(0)));
        assertThat(planetList.timestamp(), notNullValue());
        assertThat(planetList.apiVersion(), notNullValue());
        assertThat(planetList.results().stream().map(PlanetResult::url).toList(),
                everyItem(startsWith("https://")));
    }

    @Test
    public void timestampIsMappedAsLocalDateTime() {
        LocalDateTime timestamp = planetList.timestamp();

        assertThat(timestamp, notNullValue());
        assertThat(timestamp.getYear(), greaterThan(2000));
        assertThat(timestamp, lessThanOrEqualTo(LocalDateTime.now().plusDays(1)));
    }

    @Test
    public void threeMostRecentPlanetsAreIdentifiedByCreatedTimestamp() {
        List<PlanetResult> mostRecent = swapiSteps.mostRecent(planets, MOST_RECENT_COUNT);

        assertThat(mostRecent, hasSize(MOST_RECENT_COUNT));

        List<LocalDateTime> topCreated = mostRecent.stream()
                .map(planet -> planet.properties().getCreated())
                .toList();

        assertThat(topCreated, everyItem(notNullValue()));
        assertThat(topCreated.get(0), greaterThanOrEqualTo(topCreated.get(1)));
        assertThat(topCreated.get(1), greaterThanOrEqualTo(topCreated.get(2)));

        LocalDateTime cutoff = topCreated.get(MOST_RECENT_COUNT - 1);

        List<LocalDateTime> remaining = planets.stream()
                .filter(planet -> mostRecent.stream().noneMatch(top -> top.uid().equals(planet.uid())))
                .map(planet -> planet.properties().getCreated())
                .sorted(Comparator.reverseOrder())
                .toList();

        assertThat(remaining, everyItem(lessThanOrEqualTo(cutoff)));
    }

    @Test
    public void topPlanetByRotationPeriodIsValidated() {
        PlanetResult top = swapiSteps.topByRotationPeriod(planets);
        PlanetProperties properties = top.properties();

        assertThat(top.uid(), equalTo("10"));
        assertThat(properties.getName(), equalTo(TOP_ROTATION_PLANET));
        assertThat(properties.getRotationPeriod(), equalTo("27"));
        assertThat(properties.getClimate(), equalTo("temperate"));
        assertThat(properties.getTerrain(), equalTo("ocean"));
        assertThat(properties.getGravity(), equalTo("1 standard"));
        assertThat(properties.getDiameter(), equalTo("19720"));
        assertThat(properties.getCreated(), notNullValue());
        assertThat(properties.getEdited(), notNullValue());
    }

    @Test
    public void everyFetchedPlanetHasRotationPeriodNotLongerThanTheTop() {
        int topRotation = swapiSteps.topByRotationPeriod(planets).properties().rotationPeriodAsInt();

        assertThat(planets, hasSize(planetList.results().size()));
        planets.forEach(planet ->
                assertThat(planet.properties().rotationPeriodAsInt(), lessThanOrEqualTo(topRotation)));
    }
}
