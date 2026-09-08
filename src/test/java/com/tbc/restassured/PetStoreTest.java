package com.tbc.restassured;

import data.Constants;
import data.PetFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Pet;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

public class PetStoreTest extends BaseTest {

    private Pet createdPet;
    private String updatedName;

    @Override
    protected String baseUri() {
        return Constants.PETSTORE_URL;
    }

    @BeforeClass(dependsOnMethods = "setUpSpec")
    public void createPet() {
        createdPet = PetFactory.randomPet(Constants.PET_STATUS);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(createdPet)
                .when()
                .post(Constants.PET_PATH)
                .then()
                .statusCode(200);
    }

    @Test
    public void createPetReturnsSubmittedData() {
        Pet pet = PetFactory.randomPet(Constants.PET_STATUS);

        given()
                .spec(spec)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post(Constants.PET_PATH)
                .then()
                .statusCode(200)
                .body("id", equalTo((int) pet.getId()))
                .body("name", equalTo(pet.getName()))
                .body("status", equalTo(pet.getStatus()))
                .body("category.name", equalTo(pet.getCategory().getName()))
                .body("photoUrls[0]", equalTo(pet.getPhotoUrls().get(0)));
    }

    @Test
    public void createdPetIsFoundByStatus() {
        Response response = given()
                .spec(spec)
                .queryParam(Constants.STATUS_PARAM, Constants.PET_STATUS)
                .when()
                .get(Constants.PET_FIND_BY_STATUS_PATH)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Integer> ids = response.jsonPath().getList("id");
        assertThat(ids, hasItem((int) createdPet.getId()));

        Pet foundPet = response.jsonPath()
                .param("petId", createdPet.getId())
                .getObject("find { it.id == petId }", Pet.class);

        assertThat(foundPet.getName(), equalTo(createdPet.getName()));
        assertThat(foundPet.getStatus(), equalTo(createdPet.getStatus()));
        assertThat(foundPet.getCategory().getName(), equalTo(createdPet.getCategory().getName()));
        assertThat(foundPet.getPhotoUrls(), equalTo(createdPet.getPhotoUrls()));
    }

    @Test(dependsOnMethods = "createdPetIsFoundByStatus")
    public void petIsUpdatedWithFormData() {
        updatedName = createdPet.getName() + "-updated";

        given()
                .spec(spec)
                .contentType(ContentType.URLENC)
                .pathParam(Constants.PET_ID_PARAM, createdPet.getId())
                .formParam(Constants.NAME_PARAM, updatedName)
                .formParam(Constants.STATUS_PARAM, Constants.SOLD_STATUS)
                .when()
                .post(Constants.PET_BY_ID_PATH)
                .then()
                .statusCode(200);
    }

    @Test(dependsOnMethods = "petIsUpdatedWithFormData")
    public void updatedPetHasNewNameAndStatus() {
        given()
                .spec(spec)
                .pathParam(Constants.PET_ID_PARAM, createdPet.getId())
                .when()
                .get(Constants.PET_BY_ID_PATH)
                .then()
                .statusCode(200)
                .body("name", equalTo(updatedName))
                .body("status", equalTo(Constants.SOLD_STATUS));
    }

    @Test(dependsOnMethods = "createdPetIsFoundByStatus")
    public void petImageIsUploaded() throws IOException {
        File imageFile = createTempFile();
        long fileSize = imageFile.length();

        given()
                .spec(spec)
                .contentType(ContentType.MULTIPART)
                .pathParam(Constants.PET_ID_PARAM, createdPet.getId())
                .multiPart("additionalMetadata", Constants.UPLOAD_METADATA)
                .multiPart("file", imageFile)
                .when()
                .post(Constants.PET_UPLOAD_IMAGE_PATH)
                .then()
                .statusCode(200)
                .body("message", containsString(Constants.UPLOAD_METADATA))
                .body("message", containsString(imageFile.getName()))
                .body("message", containsString(fileSize + " bytes"));
    }

    private File createTempFile() throws IOException {
        Path path = Files.createTempDirectory("petstore").resolve(Constants.UPLOAD_FILE_NAME);
        Files.writeString(path, "fake pet photo content");

        return path.toFile();
    }
}