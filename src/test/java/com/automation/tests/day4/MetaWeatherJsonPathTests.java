package com.automation.tests.day4;

import com.automation.utilities.ConfigurationReader;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class MetaWeatherJsonPathTests {

    @BeforeAll
    public static void setup() {
        baseURI = ConfigurationReader.getProperty("meta.weather.uri");
    }

    /**
     * TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'New'
     * Then user verifies that payload contains 5 objects
     */
    @Test
    @DisplayName("user verifies that payload contains 5 objects")
    public void test1(){
        given().
                accept(ContentType.JSON). queryParam("query", "New").
        when().
                get("/search").
        then().
              assertThat().
                statusCode(200).
                body("", hasSize(5)).
                log().body(true);
    }

    /**
     *TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is New
     * Then user verifies that 1st object has following info:
     *  |title   |location_type|woeid  |latt_long          |
     *  |New York|City         |2459115|40.71455,-74.007118|
     */
    @Test
    @DisplayName("user verifies that 1st city has following info: New York, City, 2459115, 40.71455,-74.007118")
    public void test2(){
        given().
                accept(ContentType.JSON).
                queryParam("query", "New").
        when().
                get("/search").
        then().
                assertThat().
                statusCode(200).
                body("title[0]", is("New York")).
                body("location_type[0]", is("City")).
                body("woeid[0]", is(2459115)).
                body("latt_long[0]", is("40.71455,-74.007118")).
                log().body(true);
    }

    @Test
    @DisplayName("user verifies that 1st city has following info: New York, City, 2459115, 40.71455,-74.007118")
    public void test2_2(){
        Map<String, String> expected = new HashMap<>();
        expected.put("title", "New York");
        expected.put("location_type","City");
        expected.put("woeid", "2459115");
        expected.put("latt_long", "40.71455,-74.007118");

        Response response =
        given().
                accept(ContentType.JSON).
                queryParam("query", "New").
                when().
                get("/search");
        JsonPath jsonPath = response.jsonPath();
        //String.class, String.class will force jsonpath to return map with String as key and value
        assertEquals(expected,jsonPath.getMap("[0]", String.class, String.class));
        //for first title, title[0], but for first object, we can say just [0]
        //if one object is a key=value pair like map, collection of this objects can be represented as list of map
        List<Map<String, ?>> values = jsonPath.get();
        for(Map<String, ?> value: values){
            System.out.println(value);
        }
    }

    /**
     *TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'Las'
     * Then user verifies that payload  contains following titles:
     *  |Glasgow  |
     *  |Dallas   |
     *  |Las Vegas|
     *
 */
    @Test
    @DisplayName("user verifies that payload  contains following titles: Glasgow, Dallas, Las Vegas")
    public void test3() {
        given().
                accept(ContentType.JSON).
                queryParam("query", "Las").
        when().
                get("/search").
        then().
                assertThat().body("title", hasItems("Glasgow", "Dallas", "Las Vegas"));
    }
    /**
        *TASK
     * Given accept type is JSON
     * When users sends a GET request to "/search"
     * And query parameter is 'Las'
     * Then verify that every item in payload has location_type City
 */
    @Test
    @DisplayName("verify that every item in payload has location_type City")
    public void test4(){
       given().
                accept(ContentType.JSON).
                queryParam("query", "Las").
       when().get("/search").
       then().assertThat().
               body("location_type[0]", is("City")).
               body("location_type[1]", is("City")).
               body("location_type[2]", is("City"));
    }

    /**
     *TASK
     * Given accept type is JSON
     * When users sends a GET request to "/location"
     * And path parameter is '44418'
     * Then verify following that payload contains weather forecast sources
     * |BBC                 |
     * |Forecast.io         |
     * |HAMweather          |
     * |Met Office          |
     * |OpenWeatherMap      |
     * |Weather Underground |
     * |World Weather Online|
   */

    @Test
    @DisplayName("verify following that payload contains weather forecast sources: BBC, Forecast.io, HAMweather, Met Office, OpenWeatherMap, Weather Underground, World Weather Online")
    public void test5_mine(){

        List<String> expected = List.of("BBC", "Forecast.io", "HAMweather", "Met Office", "OpenWeatherMap", "Weather Underground", "World Weather Online");
        Response response = given().
                accept(ContentType.JSON).
            //    queryParam("id", "44418").
                    get("/location/{id}",44418);
        List<String> actual = response.thenReturn().jsonPath().getList("sources.title");
        assertEquals(expected, actual);
      //  assertTrue(expected.contains(actual), "something wrong");
        System.out.println(expected);
        System.out.println(actual);
    }

    @Test
    @DisplayName("Verify that ....")
    public void test5_3() {
        List<String> expectedCountries = List.of("BBC", "Forecast.io", "HAMweather", "Met Office", "OpenWeatherMap",
                "Weather Underground", "World Weather Online");
        Response response = given().accept(ContentType.JSON).
                pathParam("id", 44418).
                get("location/{id}");

        List<String> sources = response.thenReturn().jsonPath().getList("sources.title");
        for (int i = 0; i < expectedCountries.size(); i++) {
            assertTrue(sources.contains(expectedCountries.get(i)));
        }
        System.out.println(sources);
        System.out.println(expectedCountries);
    }


    @Test
    @DisplayName("Verify that ....")
    public void test5_2(){
        List<String> expected = List.of("BBC","Forecast.io","HAMweather","Met Office","OpenWeatherMap","Weather Underground","World Weather Online");
        List<String> actual = given().accept(ContentType.JSON).
                pathParam("id","44418").
                when().get("/{id}").thenReturn().jsonPath().getList("sources.title");
        assertEquals(expected,actual);
        System.out.println("Expected: " + expected);
        System.out.println("Actual  : " + actual);
    }

}
