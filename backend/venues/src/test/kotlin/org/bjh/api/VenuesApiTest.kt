package org.bjh.api

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import org.bjh.LocalApplicationRunner
import org.bjh.dto.RoomDto
import org.bjh.dto.VenueDto
import org.bjh.pagination.HalObject
import org.bjh.pagination.PageDto
import org.bjh.wrappers.WrappedResponse
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.lessThan
import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue


class VenuesApiTest : LocalApplicationRunner() {
    @Test
    fun deleteVenue() {
<<<<<<< HEAD

=======
>>>>>>> 9999505253c99d1c87d42bc2c5e170b08860c67d
        val data = given()
                .get().then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getList("data.list", VenueDto::class.java)[0]

        RestAssured.given()
                .delete("/${data.id}")
                .then()
                .statusCode(204)
        cacheManager.getCache("venuesCache").clear()


        RestAssured.given()
                .get("/${data.id}")
                .then()
                .statusCode(404)
    }

    @Test
    fun testPagination() {

        val pageDto = given()
                .get("/?withRooms=true&offset=0&limit=1").then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getObject("data", PageDto::class.java)
        println("\n ${pageDto._links["next"]} ${pageDto.list} \n")

        val firstVenue = pageDto.list[0]
        val nextPageDto = given()
                .get(pageDto._links["next"]!!.href.substring(7)).then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getObject("data", PageDto::class.java)

        val pageTwoVenueDto = nextPageDto.list[0]
        assertNotEquals(firstVenue, pageTwoVenueDto)
        println(message = nextPageDto.next!!.href + "j")
        println(nextPageDto.previous!!.href)
        Assert.assertThat(firstVenue, equalTo(
                given()
                        .get(nextPageDto.previous!!.href.substring(7)).then()
                        .statusCode(200)
                        .extract().body()
                        .jsonPath()
                        .getObject("data", PageDto::class.java).list[0]
        ))


    }

    @Test
    fun mergePatchVenue() {
        val venueDtoURL = createVenue()
        val oldName =
                RestAssured.given()
                        .get(venueDtoURL!!.substring(11))
                        .then()
                        .statusCode(200)
                        .extract().path<String>("data.list[0].name")
        val newName = "NEW_NAME"
        val geo = "new_geo"
        val jsonBody = "{\"name\":\"$newName\",\"geo\":\"$geo\"}"



        given().contentType("application/merge-patch+json")
                .body(jsonBody)
                .patch(venueDtoURL.substring(11))
                .then()
                .statusCode(204)

        val venueDtoList = given()
                .get(venueDtoURL.substring(11))
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", VenueDto::class.java)

        Assert.assertThat(venueDtoList[venueDtoList.size - 1].name, equalTo(newName))
        Assert.assertThat(venueDtoList[venueDtoList.size - 1].geoLocation, equalTo(geo))
    }

    @Test
    fun testWrappedResponseGetSendtFromServer() {
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
                .extract()
                .header("location")

        val data = given()
                .get(id.substring(11)).then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getList("data.list", VenueDto::class.java)[0]

        RestAssured
                .given()
                .get(id.substring(11)).then()
                .statusCode(200)
                .body("data.list[0].name", CoreMatchers.equalTo(data.name))
                .body("data.list[0].rooms.size()", CoreMatchers.equalTo(data.rooms.size))


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
                .extract().header("location")

        val data = given()
                .get("/$id?withRooms=true".substring(12))
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", VenueDto::class.java)
        val createdEnt = given().get(id.substring(11)).then().extract().body()
                .jsonPath()
                .getObject("data.list[0]", VenueDto::class.java)

        val desiredVenue = if (!data.isEmpty()) {
            data[data.size - 1]
        } else {
            VenueDto(id = null, geoLocation = null, address = null, rooms = setOf(), name = null)
        }
        Assert.assertThat(desiredVenue.id, equalTo(createdEnt.id))

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
                .extract()
                .header("location")

    }

    @Test
    fun testPatchRoomInVenue() {
        val venueId = createVenue()

        val data = given()
                .get("/$venueId?withRooms=true".substring(12)).then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getObject("data.list[0]", VenueDto::class.java)

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
                .patch("/$venueId/rooms/$roomId".substring(12))
                .then()
                .statusCode(204)
    }

    @Test
    fun testPutMethod() {
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
                .extract().header("location")

        venueDto.name = "DTO NAME"

        val dtoToFind = RestAssured
                .given()
                .get("/$id?withRooms=true".substring(12)).then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", VenueDto::class.java)[0]
        venueDto.id = dtoToFind.id

        RestAssured.given().contentType(BASE_JSON)
                .body(venueDto)
                .put(dtoToFind.id)
                .then()
                .statusCode(204)

        val dto = RestAssured
                .given()
                .get("/${venueDto.id}?withRooms=true").then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.list", VenueDto::class.java)[0]
        dto.rooms.stream().forEach {
            Assert.assertThat(it.name, equalTo(roomDto.name))
        }
        Assert.assertThat(dto.name, equalTo("DTO NAME"))
        Assert.assertThat(dto.address, equalTo(venueDto.address))
        Assert.assertThat(dto.geoLocation, equalTo(venueDto.geoLocation))


    }

    @Test
    fun testGetNonExsistingVenue() {
        RestAssured
                .given()
                .get("/1910391230219312093").then()
                .statusCode(404)
        RestAssured
                .given()
                .get("/abc}").then()
                .statusCode(400)

    }
<<<<<<< HEAD
=======

    @Test
    fun testNotNullPatchId() {
        val venueDtoUrl = createVenue()
        val oldName =
                RestAssured.given()
                        .get(venueDtoUrl!!.substring(11))
                        .then()
                        .statusCode(200)
                        .extract().path<String>("data.list[0].name")
        val createdId =
                RestAssured.given()
                        .get(venueDtoUrl!!.substring(11))
                        .then()
                        .statusCode(200)
                        .extract().path<String>("data.list[0].id")
        val newName = "NEW_NAME"
        val geo = "new_geo"
        val jsonBody = "{\"name\":\"$newName \"id\":\"$createdId\",\",\"geo\":\"$geo\"}"


        given().contentType("application/merge-patch+json")
                .body(jsonBody)
                .patch("/$createdId")
                .then()
                .statusCode(400)

    }

>>>>>>> 9999505253c99d1c87d42bc2c5e170b08860c67d
}