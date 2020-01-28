package com.automation.tests.day2;

import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

public class ORDSTests {
    //address of ORDS web service, that is running no AWS ec2.
    //dara is coming from SQL Oracle data base to this web service
    //during back-end testing with SQL developer and JDBC API
    //we were accessing data base directly
    //now, we gonna access web service
    private String baseURI = "http://ec2-35-170-246-238.compute-1.amazonaws.com:1000/ords/hr";

    //verify that status code 200
    @Test
    public void test1(){
    Response response = given().get("http://ec2-35-170-246-238.compute-1.amazonaws.com:1000/ords/hr/employees");
        System.out.println(response.getBody().asString());
        assertEquals(200,response.getStatusCode());

        System.out.println(response.prettyPrint());
    }

    //Task: get employee with id 100 and verify that response returns status code 200 and print body
    @Test
    public void test2(){
      Response response = given().header("Accept","application/json").
              get(baseURI+"/employees/100");
      int actualStatusCode = response.getStatusCode();
        System.out.println(response.prettyPrint());
        assertEquals(200, actualStatusCode);

        System.out.println(response.getHeader("Content-Type"));
    }

    //Task: perform Get request to/regions, print body and all headers
    @Test
    public void test3(){
        Response response = given().get(baseURI+"/regions");
        int actualStatusCode = response.getStatusCode();
        System.out.println(response.prettyPrint());
        assertEquals(200, actualStatusCode);

        //get specific header
        Header header = response.getHeaders().get("Content-Type");
        //print all headers one by one
        for (Header h: response.getHeaders()){
            System.out.println(h);
        }
        System.out.println(response.prettyPrint());
    }
}
