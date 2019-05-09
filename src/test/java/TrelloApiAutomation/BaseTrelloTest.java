package TrelloApiAutomation;

import groovy.json.JsonException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
//import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BaseTrelloTest {

    String boardId;
    String cardId;
    String listName;
    final String key = "0697ace29da135af1009cc535346c753 ";
    final String token = "4796c4db8c0b4d5f548a6ac73389331985e5e9eb7c6f6e588deb3b36062fa13f ";
    JSONObject requestJSon = new JSONObject();
    ArrayList<String> listIds= new ArrayList<String>();
    String listId;

    @BeforeSuite
    public void setup() {

        RestAssured.baseURI = "https://api.trello.com";


       // CreateBoard();
    }

    @AfterSuite
    public void TearDown()
    {
        DeleteBoard();
    }

@Test
    public void CreateBoard() {


        String name= "Things to Do";

        RequestSpecification requestSpecification = given()
                .queryParam("key", "0697ace29da135af1009cc535346c753")
                .queryParam("token", "4796c4db8c0b4d5f548a6ac73389331985e5e9eb7c6f6e588deb3b36062fa13f")
                .queryParam("name", name)
                    .body(requestJSon.toJSONString())
                    .log().all()
                    .contentType(ContentType.JSON);

        Response response = requestSpecification.when().post("/1/boards");
        System.out.println(response.body());

        response.then().statusCode(200).
                contentType(ContentType.JSON)
                    .body(matchesJsonSchemaInClasspath("schema.json"))
                .extract().response().jsonPath().getMap("$");


    //matchesJsonSchemaInClasspath
        Map<String, ?> map = response.body().jsonPath().getMap("$");
        boardId = (String) map.get("id");

        System.out.println("From map" + map.get("id"));
        System.out.println("From map" + map.get("name"));
        Assert.assertEquals(map.get("name"), name);

    }

    //Deleting board
    public void DeleteBoard() {
        requestSpecification = given().queryParam("key", "0697ace29da135af1009cc535346c753")
                .queryParam("token", "4796c4db8c0b4d5f548a6ac73389331985e5e9eb7c6f6e588deb3b36062fa13f")
                .pathParam("id", boardId).log().all()
                .contentType(ContentType.JSON);

        Response response = requestSpecification.when().delete("/1/boards/{id}");
     //   Response response = requestSpecification.when().delete("https://api.trello.com/1/boards/{id}");
        System.out.println(response.body());

        response.then().statusCode(200).contentType(ContentType.JSON).extract()
                .response().body().jsonPath().getMap("$");


    }


    public JSONObject fillQueryParams()
    {
        JSONObject requestJSon = new JSONObject();

        try {

            requestJSon.put("key", "0697ace29da135af1009cc535346c753");
            requestJSon.put("token", "4796c4db8c0b4d5f548a6ac73389331985e5e9eb7c6f6e588deb3b36062fa13f");
            requestJSon.put("name", "Things to do");
        } catch (JsonException e) {
            e.printStackTrace();
        }

        return requestJSon;
    }
}
