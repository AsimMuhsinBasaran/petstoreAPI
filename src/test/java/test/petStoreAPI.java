package test;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.testng.annotations.*;
import pojo.*;
import utilities.TestBaseRapor;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class petStoreAPI extends TestBaseRapor {
    Faker faker = new Faker();
    Long id = ThreadLocalRandom.current().nextLong(200, 500);
    String petName = faker.pokemon().name();
    String updatedPetName = faker.pokemon().name();
    String petCategory = faker.animal().name();
    String tagName = faker.funnyName().name();

    @BeforeTest
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test(priority = 0)
    public void createRequest() {
        extentTest = extentReports.createTest
                ("createTest", "Kullanici yeni bir pet create edebilmeli.");
        Category category = new Category();
        category.setId(11L);
        category.setName(petCategory);

        Tag tag = new Tag();
        tag.setId(11L);
        tag.setName(tagName);

        PetPojo pet = new PetPojo();
        pet.setId(id);
        pet.setName(petName);
        pet.setStatus("available");
        pet.setCategory(category);
        pet.setTags(List.of(tag));
        pet.setPhotoUrls(List.of("https://www.petcanlar.com/Data/Blog/43.jpg"));

        JsonPath jsonPath = RestAssured.given()
                .when()
                .contentType(ContentType.APPLICATION_JSON.getMimeType())
                .with()
                .body(pet)//add pet object
                .post("/pet")
                .then()
                .assertThat()
                .statusCode(200)
                .log()
                .body()
                .assertThat()
                .extract().body().jsonPath();

        get(baseURI + "/pet/" + pet.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .log()
                .body()
                .body("id", equalTo(pet.getId().intValue()))
                .body("name", equalToCompressingWhiteSpace(pet.getName()))
                .body("status", equalToCompressingWhiteSpace(pet.getStatus()))
                .body("category.id", equalTo(pet.getCategory().getId().intValue()))
                .body("category.name", equalTo(pet.getCategory().getName()))
                .body("tags[0].id", equalTo(pet.getTags().get(0).getId().intValue()))
                .body("tags[0].name", equalTo(pet.getTags().get(0).getName()))
                .body("photoUrls[0]", containsStringIgnoringCase("pet"));
        extentTest.info("Basarili bir sekilde pet create edildi.");
    }

    @Test(priority = 1)
    public void updateRequest() {
        extentTest = extentReports.createTest
                ("updateTest", "Kullanici mevcut bir pet'i update edebilmeli.");
        RestAssured.given()
                .when()
                .contentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                .formParam("name", updatedPetName)
                .formParam("status", "pending")
                .post("/pet/" + id)
                .then()
                .log()
                .all()
                .assertThat()
                .statusCode(200);
        extentTest.info("Basarili bir sekilde pet update edildi.");
    }

    @Test(priority = 2)
    public void getRequest() {
        extentTest = extentReports.createTest
                ("readTest", "Kullanici mevcut bir pet'i read edebilmeli.");
        JsonPath jsonPath = RestAssured.given()
                .when()
                .get("/pet/" + id)
                .then()
                .assertThat()
                .statusCode(200)
                .assertThat()
                .extract().body().jsonPath();

        assertEquals(updatedPetName, jsonPath.get("name"));
        assertEquals("pending", jsonPath.get("status"));
        extentTest.info("Basarili bir sekilde pet read edildi.");
    }

    @Test(priority = 3,enabled = true)
    public void deleteRequest() {
        extentTest = extentReports.createTest
                ("deleteTest", "Kullanici mevcut bir pet'i delete edebilmeli.");
        RestAssured.given()
                .when()
                .header("api_key", "token")
                .delete("/pet/" + id)
                .then()
                .assertThat()
                .statusCode(200);
        extentTest.info("Basarili bir sekilde pet delete edildi.");
    }


}
