package steps;

import api.client.PetStoreOrderApi;
import data.models.requests.petstore.OrderRequest;
import data.models.responses.petstore.OrderResponse;
import io.qameta.allure.Step;

public class PetStoreOrderSteps {

    private static final int OK = 200;

    private final PetStoreOrderApi petStoreOrderApi = new PetStoreOrderApi();

    @Step("Place a store order")
    public OrderResponse createOrder(OrderRequest order) {
        return petStoreOrderApi.createOrder(order)
                .then()
                .statusCode(OK)
                .extract()
                .as(OrderResponse.class);
    }

    @Step("Get the store order by id")
    public OrderResponse getOrder(long orderId) {
        return petStoreOrderApi.getOrder(orderId)
                .then()
                .statusCode(OK)
                .extract()
                .as(OrderResponse.class);
    }
}
