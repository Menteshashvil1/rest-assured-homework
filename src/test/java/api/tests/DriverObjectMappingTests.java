package api.tests;

import data.models.responses.f1.Driver;
import org.testng.annotations.Test;
import steps.DriverSteps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;

public class DriverObjectMappingTests {

    private static final Driver EXPECTED_FIRST_DRIVER = new Driver(
            "albon",
            "23",
            "ALB",
            "http://en.wikipedia.org/wiki/Alexander_Albon",
            "Alexander",
            "Albon",
            "1996-03-23",
            "Thai");

    private final DriverSteps driverSteps = new DriverSteps();

    @Test
    public void firstDriverMatchesLocallyInitialisedDriver() {
        Driver actual = driverSteps.getFirstDriver();

        assertThat(actual, notNullValue());
        assertThat(actual, samePropertyValuesAs(EXPECTED_FIRST_DRIVER));
    }
}
