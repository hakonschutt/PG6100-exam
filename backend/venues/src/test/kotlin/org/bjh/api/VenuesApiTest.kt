package org.bjh.api

import com.google.gson.JsonObject
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.bjh.LocalApplicationRunner
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueDto
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Test


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
    @Test
    fun deleteVenue() {
        val sizeBefore = RestAssured.given()
                .get()
                .then()
                .statusCode(200)
                .extract().path<Int>("data.size()")
        val createdVenueId = createVenue()
        val sizeAfterAddition =
                RestAssured.given()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract().path<Int>("data.size()")

        assert(sizeBefore < sizeAfterAddition)


        RestAssured.given()
                .delete("/${createdVenueId}")
                .then()
                .statusCode(200)


        val sizeAfterDeletion = RestAssured.given()
                .get()
                .then()
                .statusCode(200)
                .extract().path<Int>("data.size()")


        Assert.assertThat(sizeAfterDeletion, equalTo(sizeBefore))


    }

    @Test
    fun mergePatchVenue() {
        val venueDtoId = createVenue()
        val oldName =
                RestAssured.given()
                        .get("/$venueDtoId")
                        .then()
                        .statusCode(200)
                        .extract().path<String>("data[0].name")
        val newName = "NEW_NAME"
        val jsonBody = "{\"name\":\"$newName\"}"



        given().contentType("application/merge-patch+json")
                .body(jsonBody)
                .patch("/$venueDtoId")
                .then()
                .statusCode(204)

        println("RESPONSE: " + given().get("/$venueDtoId")
                .then()
                .statusCode(200)
                .extract().response().asString())

        val updatedName = given().get("/$venueDtoId")
                .then()
                .statusCode(200)
                .extract().path<String>("data.name")

        Assert.assertThat(updatedName, equalTo(newName))

    }

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

        RestAssured.given()
                .get()
                .then()
                .statusCode(200)
                //here we expect 4 since we are getting a wrapped object back
                .body("size()", CoreMatchers.equalTo(4))
    }

    @Test
    fun testCreateVenue() {
        val roomName = "sal-2"
        val rows = 1
        val cols = 1
        val roomDto = RoomDto(id = null, name = roomName, rows = rows, columns = cols)

        val name = "root"
        val geo = "home"
        val address = "127.0.0.1"
        val venueDto = VenueDto(id = null, geoLocation = geo, name = name, rooms = setOf(roomDto), address = address)

        val id = RestAssured.given().contentType(BASE_JSON)
                .body(venueDto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        RestAssured
                .given()
                .get().then()
                .statusCode(200)
                .body("data[0].id", CoreMatchers.equalTo(id))

        RestAssured
                .given()
                .get().then()
                .statusCode(200)
                .body("data[0].rooms[0].name", CoreMatchers.equalTo(roomName))


    }

    @Test
    fun testCreateVenueWithMultipleRooms() {
        val roomName = "sal-1"
        val rows = 2
        val cols = 2
        val roomDto = RoomDto(id = null, name = roomName, rows = rows, columns = cols)

        val roomName2 = "sal-2"
        val rows2 = 1
        val cols2 = 1
        val roomDto2 = RoomDto(id = null, name = roomName2, rows = rows2, columns = cols2)

        val name = "root"
        val geo = "home"
        val address = "127.0.0.1"

        val venueDto = VenueDto(id = null, geoLocation = geo, name = name, rooms = setOf(roomDto2, roomDto), address = address)

        val id = RestAssured.given().contentType(BASE_JSON)
                .body(venueDto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

        val data = RestAssured
                .given()
                .get().then()
                .statusCode(200)
                .extract().body().jsonPath().getList("data", VenueDto::class.java)
        val desiredVenue = data[data.size - 1]

        Assert.assertThat(desiredVenue.id, equalTo(id))
//
        val roomsNamesExsist = desiredVenue.rooms.stream().allMatch {
            it.name == roomName || it.name == roomName2
        }
        Assert.assertThat((roomsNamesExsist), equalTo(true))
    }

    private fun createVenue(): String? {
        val roomName2 = "sal-1"
        val rows2 = 1
        val cols2 = 1
        val roomDto2 = RoomDto(id = null, name = roomName2, rows = rows2, columns = cols2)

        val name = "root"
        val geo = "home"
        val address = "127.0.0.1"

        val venueDto = VenueDto(id = null, geoLocation = geo, name = name, rooms = setOf(roomDto2), address = address)

        return RestAssured.given().contentType(BASE_JSON)
                .body(venueDto)
                .post()
                .then()
                .statusCode(201)
                .extract().asString()

    }

    @Test
    fun testPatchRoomInVenue() {
        val venueId = createVenue()

        val data = given()
                .get("/$venueId").then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getObject("data", VenueDto::class.java)

        val roomToEdit = data.rooms.map { it }[0]
        val roomId = roomToEdit.id
        val oldRoomCol = roomToEdit.columns
        val newRoomCol = if (oldRoomCol == null) {
            0
        } else {
            20
        }

        given().contentType("application/merge-patch+json")
                .body("{\"columns\":$newRoomCol}")
                .patch("/$venueId/rooms/$roomId")
                .then()
                .statusCode(204)


    }
}