package com.automation.tests.day2;

import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ExchangeRatesAPITests {

    private String baseURI = "http://api.openrates.io/";

    @Test
    public void test1(){
        Response response= given().baseUri(baseURI+"latest").get();
        assertEquals(200, response.getStatusCode());
        response.body().prettyPrint();
       // System.out.println(response.prettyPrint());
    }

    @Test
    public void test2(){
        Response response = given().get(baseURI+"latest");
        //verify that content type is json
        assertEquals(200, response.getStatusCode());
        //verify that data is coming as json
        assertEquals("application/json", response.getHeader("Content-Type"));
        //or like this
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void test3(){
        //#TASK: get currency exchange rate for dollar. By default it's euro.
//        Response response = given().get(baseURI+"latest?base=USD");
        Response response = given().baseUri(baseURI).
                basePath("latest").
                queryParam("base", "USD").get();
        assertEquals(200, response.getStatusCode());
        System.out.println(response.prettyPrint());
    }

    @Test
    public void test4(){
        //Task: verify that response body for latest currency rates contains today's date ("date": "2020-01-23" | yyyy-mm-dd)
        Response response = given().baseUri(baseURI+"latest").
                queryParam("base", "GBP").get();
        String todaysDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println("Today's date "+todaysDate);
        assertTrue(response.getBody().asString().contains(todaysDate));
        System.out.println(response.prettyPrint());
    }

    //let's get currency exchange rate for year 2000
    //GET https://api.exchangeratesapi.io/history?start_at=2018-01-01&end_at=2018-09-01&base=USD
    @Test
    public void test5(){
        Response response = given().baseUri(baseURI+"history").
                queryParam("start_at", "2000-01-01").
                queryParam("end_at", "2000-12-31").
                queryParam("base", "USD").
                queryParam("symbols", "EUR", "GBP", "JPY").get();
      //  assertTrue(response.getBody().asString().contains("2000-01-01"));
        System.out.println(response.prettyPrint());
    }

    /**
     * Given request parameter "base" is "USD"
     * When user sends request to "api.openrates.io"
     * Then response code should be 200
     * And response body must contain ""base": "USD""
     */
    @Test
    public void test6(){

        Response response = given().
                baseUri(baseURI+"latest").
                queryParam("base", "USD").
                get();
        String body = response.getBody().asString();
        assertEquals(200, response.getStatusCode());
        assertTrue(body.contains("\"base\":\"USD\""));
    }

}
