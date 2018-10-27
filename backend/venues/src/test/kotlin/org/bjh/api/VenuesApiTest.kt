package org.bjh.api

import io.restassured.RestAssured
import org.bjh.LocalApplicationRunner
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueDto
import org.hamcrest.CoreMatchers
import org.junit.Test

import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner


class VenuesApiTest : LocalApplicationRunner() {

//    @Test
//    fun getAllVenues() {
//    }
//
//    @Test
//    fun getVenue() {
//    }
//
//    @Test
//    fun createVenue() {
//    }
//
//    @Test
//    fun deleteVenue() {
//    }
//
//    @Test
//    fun mergePatch() {
//    }

    @Test
    fun testCreateAndGetWithNewFormat() {
        val roomName = "sal-1"
        val rows = 1
        val cols = 1
        val roomDto = RoomDto(id = null, name = roomName, rows = rows, columns = cols)

        val name = "root"
        val geo = "home"
        val address = "127.0.0.1"


        val venueDto = VenueDto(id = null, geoLocation = geo, name = name, rooms = setOf(roomDto), address = address)

        //no news
        RestAssured.given()
                .get()
                .then()
                .statusCode(200)
                //here we expect 4 since we are getting a wrapped object back
                .body("size()", CoreMatchers.equalTo(4))

//        //create a news
//        val id = RestAssured.given().contentType(V2_NEWS_JSON)
//                .body(dto)
//                .post()
//                .then()
//                .statusCode(201)
//                .extract().asString()
//
//        //should be 1 news now
//        RestAssured.given().accept(V2_NEWS_JSON)
//                .get()
//                .then()
//                .statusCode(200)
//                .body("size()", CoreMatchers.equalTo(1))
//
//        //1 news with same data as the POST
//        RestAssured.given().accept(V2_NEWS_JSON)
//                .pathParam("id", id)
//                .get("/{id}")
//                .then()
//                .statusCode(200)
//                .body("newsId", CoreMatchers.equalTo(id))
//                .body("authorId", CoreMatchers.equalTo(author))
//                .body("text", CoreMatchers.equalTo(text))
//                .body("country", CoreMatchers.equalTo(country))
//    }

    }
}