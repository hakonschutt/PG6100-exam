package org.bjh

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.bjh.dto.VenueDto
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(VenuesApplicationRunner::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class LocalApplicationRunner {


    @LocalServerPort
    protected var port = 0

    @Before
    @After
    fun clean() {

        // RestAssured configs shared by all the tests
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/venues"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        /*
           Here, we read each resource (GET), and then delete them
           one by one (DELETE)
         */
//        val list = RestAssured.given().accept(ContentType.JSON).get()
//                .then()
//                .statusCode(200)
//                .extract()
//                .`as`(Array<VenueDto>::class.java)
//                .toList()


        /*
            Code 204: "No Content". The server has successfully processed the request,
            but the return HTTP response will have no body.
         */
//        list.stream().forEach {
//            RestAssured.given().pathParam("id", it.id)
//                    .delete("/{id}")
//                    .then()
//                    .statusCode(204)
//        }
//
//        RestAssured.given().get()
//                .then()
//                .statusCode(200)
//                .body("size()", CoreMatchers.equalTo(0))
//    }
    }
}
