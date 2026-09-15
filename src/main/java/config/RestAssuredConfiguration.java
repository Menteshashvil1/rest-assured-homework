package config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.mapper.ObjectMapperType;

public final class RestAssuredConfiguration {

    private static boolean configured;

    private RestAssuredConfiguration() {}

    public static synchronized void configure() {
        if (configured) {
            return;
        }

        RestAssured.filters(new AllureRestAssured()
                .setRequestTemplate("http-request.ftl")
                .setResponseTemplate("http-response.ftl"));

        RestAssured.config = RestAssured.config().objectMapperConfig(
                new ObjectMapperConfig(ObjectMapperType.JACKSON_2)
                        .jackson2ObjectMapperFactory((type, charset) -> objectMapper()));

        configured = true;
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }
}
