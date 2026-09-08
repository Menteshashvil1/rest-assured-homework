package com.tbc.restassured;

import data.Constants;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FormulaDriversTest extends BaseTest {

    private static final int PAGE_LIMIT = 100;

    @Override
    protected String baseUri() {
        return Constants.ERGAST_URL;
    }

    @Test
    public void seriesAndSeasonAreCorrect() {
        given()
                .spec(spec)
                .queryParam("limit", PAGE_LIMIT)
                .when()
                .get(Constants.DRIVERS_PATH)
                .then()
                .statusCode(200)
                .body("MRData.series", equalTo("f1"))
                .body("MRData.DriverTable.season", equalTo("2025"));
    }

    @Test
    public void driverCountMatchesReportedTotal() {
        Response response = fetchDrivers();

        int reportedTotal = Integer.parseInt(response.jsonPath().getString("MRData.total"));
        int actualDrivers = response.jsonPath().getList("MRData.DriverTable.Drivers").size();

        assertThat(actualDrivers, equalTo(reportedTotal));
    }

    @Test
    public void firstDriverBornBefore1990HasExpectedName() {
        String fullName = fetchDrivers().jsonPath()
                .param("cutoffDate", "1990-01-01")
                .getString("MRData.DriverTable.Drivers.find { it.dateOfBirth < cutoffDate }"
                        + ".with { it.givenName + ' ' + it.familyName }");

        assertThat(fullName, equalTo("Fernando Alonso"));
    }

    @Test
    public void atLeastEightDriversBornAfter2000() {
        List<String> names = fetchDrivers().jsonPath()
                .param("cutoffDate", "2000-01-01")
                .getList("MRData.DriverTable.Drivers.findAll { it.dateOfBirth > cutoffDate }"
                        + ".collect { it.givenName + ' ' + it.familyName }");

        assertThat(names, hasSize(greaterThanOrEqualTo(8)));
    }

    @Test
    public void exactlyThreeFrenchDrivers() {
        assertThat(findDriversByNationality("French"), hasSize(3));
    }

    @Test
    public void atLeastFiveDriversWithFamilyNameStartingWithAOrB() {
        List<String> familyNames = fetchDrivers().jsonPath()
                .getList("MRData.DriverTable.Drivers"
                        + ".findAll { it.familyName.startsWith('A') || it.familyName.startsWith('B') }"
                        + ".collect { it.familyName }");

        assertThat(familyNames, hasSize(greaterThanOrEqualTo(5)));
    }

    @Test
    public void britishDriversBornAfter1990() {
        List<String> names = fetchDrivers().jsonPath()
                .param("targetNationality", "British")
                .param("cutoffDate", "1990-01-01")
                .getList("MRData.DriverTable.Drivers"
                        + ".findAll { it.nationality == targetNationality && it.dateOfBirth > cutoffDate }"
                        + ".collect { it.givenName + ' ' + it.familyName }");

        assertThat(names, hasSize(greaterThanOrEqualTo(3)));
    }

    @Test
    public void driversWithLowNumberOrLongSurname() {
        List<String> names = fetchDrivers().jsonPath()
                .param("maxNumber", 10)
                .param("minNameLength", 7)
                .getList("MRData.DriverTable.Drivers.findAll { "
                        + "(it.permanentNumber && it.permanentNumber.toInteger() < maxNumber) "
                        + "|| it.familyName.length() > minNameLength }"
                        + ".collect { it.givenName + ' ' + it.familyName }");

        names.forEach(System.out::println);

        assertThat(names, hasSize(greaterThanOrEqualTo(5)));
    }

    @Test
    public void britishDriversIncludeGeorge() {
        assertThat(findDriversByNationality("British"), hasItem(containsString("George")));
    }

    @Test
    public void brazilianDriversExist() {
        assertThat(findDriversByNationality("Brazilian"), is(not(empty())));
    }

    @Test
    public void frenchDriversIncludePierreGasly() {
        assertThat(findDriversByNationality("French"), hasItem("Pierre Gasly"));
    }

    private Response fetchDrivers() {
        return given()
                .spec(spec)
                .queryParam("limit", PAGE_LIMIT)
                .when()
                .get(Constants.DRIVERS_PATH)
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    private List<String> findDriversByNationality(String nationality) {
        return fetchDrivers().jsonPath()
                .param("targetNationality", nationality)
                .getList("MRData.DriverTable.Drivers.findAll { it.nationality == targetNationality }"
                        + ".collect { it.givenName + ' ' + it.familyName }");
    }
}