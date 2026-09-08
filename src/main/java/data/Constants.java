package data;

public class Constants {
    public static final String ERGAST_URL = "https://api.jolpi.ca";
    public static final String DRIVERS_PATH = "/ergast/f1/2025/drivers/";
    public static final String PETSTORE_URL = "https://petstore.swagger.io/v2";

    public static final String PET_PATH = "/pet";
    public static final String PET_FIND_BY_STATUS_PATH = "/pet/findByStatus";
    public static final String PET_BY_ID_PATH = "/pet/{petId}";
    public static final String PET_UPLOAD_IMAGE_PATH = "/pet/{petId}/uploadImage";

    public static final String PET_STATUS = "pending";
    public static final String SOLD_STATUS = "sold";

    public static final String STATUS_PARAM = "status";
    public static final String NAME_PARAM = "name";
    public static final String PET_ID_PARAM = "petId";

    public static final String UPLOAD_METADATA = "uploaded-by-nodar";
    public static final String UPLOAD_FILE_NAME = "pet-photo.txt";
}
