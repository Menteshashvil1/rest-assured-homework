package api.client;

import data.Constants;
import data.models.requests.petstore.OrderRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PetStoreOrderApi extends BaseApi {

    public Response createOrder(OrderRequest order) {
        return given()
                .spec(PETSTORE3_SPEC)
                .body(order)
                .when()
                .post(Constants.STORE_ORDER_PATH);
    }

    public Response getOrder(long orderId) {
        return given()
                .spec(PETSTORE3_SPEC)
                .pathParam(Constants.ID_PARAM, orderId)
                .when()
                .get(Constants.STORE_ORDER_PATH + "/{id}");
    }
}
