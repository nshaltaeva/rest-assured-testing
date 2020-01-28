package com.automation.tests.day3;

import com.automation.utilities.ConfigurationReader;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import static org.junit.jupiter.api.Assertions.*;

public class ORDSTestsDay3 {

    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = ConfigurationReader.getProperty("ords.uri");
    }

    @Test
    public void test1(){
        given().accept("applciation/json").
                get("/employees").
                then().
                assertThat().statusCode(200).
                and().assertThat().contentType("application/json").
                log().all(true);

    }

    @Test
    public void test2(){

        given().
                accept("application/json").
                pathParam("id", 100).
                when().get("/employees/{id}").
                then().assertThat().statusCode(200).
                and().assertThat().body("employee_id", is(100),
                "department_id", is(90),
                "last_name", is("King")).
                log().all(true);
        //body ("phone_number") --> 515.123.4567
        //is is coming from ---> import static org.hamcrest.Matchers.*;
        //log().all  Logs everything in the response, including e.g. headers,
        // cookies, body with the option to pretty-print the body if the content-type is
    }
    /**
     * given path parameter is "/regions/{id}"
     * when user makes get request
     * and region id is equals to 1
     * then assert that status code is 200
     * and assert that region name is Europe
     */
    @Test
    public void test3(){
        given().
                accept("application/json").
                pathParam("id",1).
        when().
                get("/regions/{id}").
        then().
                assertThat().statusCode(200).
                assertThat().body("region_name", is("Europe")).
                time(lessThan(3L), TimeUnit.SECONDS).
                //extract().response().prettyPrint();
                log().body(true);//log body in pretty format. all = header + body + status code

    }

    @Test
    public void test4(){
        JsonPath json = given().
                accept("application/json").
        when().
                get("/employees").
        thenReturn().jsonPath();
        String nameOfFirstEmployee = json.getString("items[0].first_name");
        String nameOfLastEmployee = json.getString("items[-1].first_name"); //-1 is last index
        System.out.println("First employee name: "+nameOfFirstEmployee);
        System.out.println("Last employee name: "+nameOfLastEmployee);

        Map<String, String> firstEmployee = json.get("items[0]");
        System.out.println(firstEmployee);
        for(Map.Entry<String, ?> entry : firstEmployee.entrySet()){
            System.out.println("key: "+entry.getKey()+", value: "+entry.getValue());
        }
        System.out.println("=====================================");
        List<String> lastNames = json.get("items.last_name");
        for(String str: lastNames){
            System.out.println("Last name: "+str);
        }

    }

    @Test
    public void test5(){
        //write a code to get info from countries as List<Map<String,?>>
        JsonPath json =given().
                accept("application/json").
        when().
                get("/countries").prettyPeek().jsonPath();
        List<Map<String, ?>> allCountries = json.get("items");
        for (Map<String, ?> map: allCountries){
            System.out.println(map);
        }
    }

    //get collection of employee's salaries
    //then sort it
    //and print
    @Test
    public void test6(){
        List<Integer> salaries = given().
                accept("application/json").
                when().
                get("/employees").
                thenReturn().jsonPath().get("items.salary");
        Collections.sort(salaries);
        Collections.reverse(salaries);
        System.out.println(salaries);
    }


    //get collection of phone numbers from employees
    //and replace all dots "." in every phone number with dash "-"
    @Test
    public void test7(){
        List<String> phoneNumbers = given().
                accept("application/json").
                when().
                get("/employees").
                thenReturn().jsonPath().get("items.phone_number");

        phoneNumbers.replaceAll(phone -> phone.replace(".","-"));
        System.out.println(phoneNumbers);


    }



    }



