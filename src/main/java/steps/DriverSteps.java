package steps;

import api.client.DriverApi;
import data.models.response.Driver;

public class DriverSteps {

    private static final int OK = 200;
    private static final String FIRST_DRIVER_PATH = "MRData.DriverTable.Drivers[0]";

    private final DriverApi driverApi = new DriverApi();

    public Driver getFirstDriver() {
        return driverApi.getDrivers()
                .then()
                .statusCode(OK)
                .extract()
                .jsonPath()
                .getObject(FIRST_DRIVER_PATH, Driver.class);
    }
}
