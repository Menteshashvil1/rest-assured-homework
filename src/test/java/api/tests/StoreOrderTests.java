package api.tests;

import config.RestAssuredConfiguration;
import data.models.requests.petstore.OrderRequest;
import data.models.responses.petstore.OrderResponse;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import steps.PetStoreOrderSteps;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.stringContainsInOrder;

@Epic("Advanced object mapping")
@Feature("Petstore store order")
public class StoreOrderTests {

    private static final int QUANTITY = 7;
    private static final String STATUS = "approved";

    private final PetStoreOrderSteps orderSteps = new PetStoreOrderSteps();

    private long orderId;
    private long petId;
    private OffsetDateTime shipDate;

    @BeforeClass(alwaysRun = true)
    public void prepareOrderData() {
        orderId = ThreadLocalRandom.current().nextLong(1, 10);
        petId = ThreadLocalRandom.current().nextLong(100_000, 999_999);
        shipDate = OffsetDateTime.now(ZoneOffset.UTC).plusDays(5).truncatedTo(ChronoUnit.MILLIS);
    }

    @Test
    public void orderIsCreatedFromFluentBuilder() {
        OrderResponse created = orderSteps.createOrder(newOrder());

        assertThat(created, allOf(
                hasProperty("id", equalTo(orderId)),
                hasProperty("petId", equalTo(petId)),
                hasProperty("quantity", equalTo(QUANTITY)),
                hasProperty("status", equalTo(STATUS)),
                hasProperty("complete", equalTo(true))));

        assertThat(created.getShipDate(), notNullValue());
        assertThat(created.getShipDate().toInstant(), equalTo(shipDate.toInstant()));
    }

    @Test
    public void requestSerializesFieldsInTheConfiguredOrder() throws Exception {
        String json = RestAssuredConfiguration.objectMapper().writeValueAsString(newOrder());

        assertThat(json, stringContainsInOrder(
                "status", "complete", "shipDate", "quantity", "petId", "id"));

        assertThat(json.indexOf("status"), lessThan(json.indexOf("id\"")));
    }

    @Test
    public void nullFieldsAreOmittedFromTheOrderBody() throws Exception {
        OrderRequest partial = OrderRequest.builder()
                .id(orderId)
                .status(STATUS)
                .build();

        String json = RestAssuredConfiguration.objectMapper().writeValueAsString(partial);

        assertThat(json, equalTo("{\"status\":\"" + STATUS + "\",\"id\":" + orderId + "}"));
    }

    private OrderRequest newOrder() {
        return OrderRequest.builder()
                .id(orderId)
                .petId(petId)
                .quantity(QUANTITY)
                .shipDate(shipDate)
                .status(STATUS)
                .complete(true)
                .build();
    }
}
