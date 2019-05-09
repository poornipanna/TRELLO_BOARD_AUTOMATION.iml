package TrelloApiAutomation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;


import java.util.*;

import static io.restassured.RestAssured.*;

public class CreateTrelloBoardTest extends BaseTrelloTest
{


 //   @Test(priority=1)
    public void GetListsOfBoard()
    {

        RequestSpecification requestSpecification =  given()
                .pathParam("boardID",boardId)
                .queryParam("key", key)
                .queryParam("token", token)
                .body(requestJSon.toJSONString())
                .log().all()
                .contentType(ContentType.JSON);


        Response response = requestSpecification.when().
                        get("boards/{boardID}/lists/");

        response.then()
                .statusCode(200);

        System.out.println(response.statusCode());

        List<Map<String, ?>> listInfo = response.jsonPath().getList("$");
        for(int i = 0; i < listInfo.size(); i++) {
            System.out.println(listInfo.get(i).get("id").toString());
            System.out.println(listInfo.get(i).get("name").toString());
        }
        listId = listInfo.get(0).get("id").toString();
        listName = listInfo.get(0).get("name").toString();
        System.out.println("List ID: " + listId);
        System.out.println("List Name: " + listName);


    }

//    @Test(priority=2)
    public void createCardInList()
    {

        RequestSpecification requestSpecification = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", "CardName")
                .queryParam("idList", listId)
                .contentType(ContentType.JSON)
                .log().all();

        Response response = requestSpecification.when().
                post("cards");
        System.out.println("Response Body: " + response.body());
        System.out.println("Response Status Code: " + response.statusCode());


        response.then()
                .statusCode(200);
            /*    .contentType(ContentType.JSON)
                .extract()
                 .response()
                .jsonPath()
                .getMap("$");*/
        Map<Object, Object> map = response.jsonPath().getMap("$");

        cardId = map.get("id").toString();
        System.out.println(cardId);

    }



    //@Test
    public void updateCardDetails()
    {

        System.out.println("updateCardDetails");
        String newNameOfCard= "Send a Mail";

        /*
        requestSpecification = given()
                .body(requestJSon.toJSONString())
                .log().all()
                .contentType(ContentType.JSON);

        */

        RequestSpecification requestSpecification = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .pathParam("id", cardId)
                .queryParam("name",newNameOfCard)
                .body(requestJSon.toJSONString())
                .log().all()
                .contentType(ContentType.JSON);

        Response response = requestSpecification.when().
                put("1/cards/{cardId}");
        response.then()
                .statusCode(200);

        Map<Object, Object> map = response.jsonPath().getMap("$");

        cardId = map.get("id").toString();
        System.out.println("CardID: " + cardId);


    }

    //@Test
    public void deleteCard()
    {
        System.out.println("deleteCard");
        RequestSpecification requestSpecification = given()
                .queryParam("key", key)
                .queryParam("token", token)
                .pathParam("id", cardId)
                .body(requestJSon.toJSONString())
                .log().all()
                .contentType(ContentType.JSON);

        Response response = requestSpecification.when().delete("/1/cards/{cardId}");

        System.out.println(response.body());

        response.then().statusCode(200).contentType(ContentType.JSON).extract()
                .response().body().jsonPath().getMap("$");


    }


}