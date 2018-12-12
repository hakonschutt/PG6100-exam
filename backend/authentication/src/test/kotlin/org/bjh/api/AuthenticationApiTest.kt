package org.bjh.api

import io.restassured.RestAssured
import org.bjh.TestBase
import org.bjh.dto.AuthenticationDto
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test

class AuthenticationApiTest : TestBase() {

    @Test
    fun testDeleteUser() {

        val data = RestAssured.given()
                .get().then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getList("data.list", AuthenticationDto::class.java)[0]

        RestAssured.given()
                .delete("/${data.email}")
                .then()
                .statusCode(204)


        RestAssured.given()
                .get("/${data.email}")
                .then()
                .statusCode(404)
    }

    @Test
    fun testGetUser() {
        val data = RestAssured.given()
                .get().then()
                .statusCode(200)
                .extract().body()
                .jsonPath()
                .getList("data.list", AuthenticationDto::class.java)[0]

        RestAssured.given()
                .delete("/${data.email}")
                .then()
                .statusCode(204)
    }

    @Test
    fun testGetAllUsers() {
        RestAssured.given()
                .get()
                .then()
                .statusCode(200)
                .body("data.list.size()", equalTo(3))
    }

    @Test
    fun testDeleteNotExistingUser() {
        RestAssured.given()
                .delete("/19232180921382380921@gmail.notfound.com")
                .then()
                .statusCode(404)
    }

//    @Test
//    fun testPagination() {
//
//        val pageDto = given()
//                .get("/?offset=0&limit=1").then()
//                .statusCode(200)
//                .extract().body()
//                .jsonPath()
//                .getObject("data", PageDto::class.java)
//        println("\n ${pageDto._links["next"]} ${pageDto.list} \n")
//
//        val firstUserPage = pageDto.list[0]
//        val nextPageDto = given()
//                .get(pageDto._links["next"]!!.href.substring(7)).then()
//                .statusCode(200)
//                .extract().body()
//                .jsonPath()
//                .getObject("data", PageDto::class.java)
//
//        val prevPage = given()
//                .get(nextPageDto.previous!!.href.substring(7)).then()
//                .statusCode(200)
//                .extract().body()
//                .jsonPath()
//                .getObject("data", PageDto::class.java).list[0]
//        Assert.assertThat(firstUserPage, equalTo(
//                prevPage
//        ))
//
//
//    }

    @Test
    fun testCreateUser() {
        val id = RestAssured.given().contentType("application/json;charset=UTF-8")
                .body(AuthenticationDto(email = "foo@gmail.com"))
                .post()
                .then()
                .statusCode(201)
                .extract()
                .header("location")
        println(id)
    }
}