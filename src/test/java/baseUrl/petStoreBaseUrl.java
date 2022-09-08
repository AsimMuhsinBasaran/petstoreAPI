package baseUrl;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;

public class petStoreBaseUrl {

    protected RequestSpecification spec;
    @BeforeTest
    public void setUp(){
        spec=new RequestSpecBuilder().setBaseUri("https://petstore.swagger.io/v2").build();
    }
}
