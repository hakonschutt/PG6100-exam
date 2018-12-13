package org.bjh.api

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.TestBase
import org.bjh.dto.UserDto
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Test

class AuthenticationApiTest : TestBase() {


    @Test
    fun testUnauthorizedAccess() {
        given().get()
                .then()
                .statusCode(401)
    }

    @Test
    fun testLogin() {

        val name = "foo"
        val password = "bar"

        checkAuthenticatedCookie("invalid cookie", 401)

        val cookie = registerUser(name, password)

        given().get()
                .then()
                .statusCode(401)

        given().cookie("SESSION", cookie)
                .get("/user")
                .then()
                .statusCode(200)
                .body("name", equalTo(name))

        val basic = given().auth().basic(name, password)
                .get("/user")
                .then()
                .statusCode(200)
                .cookie("SESSION") // new SESSION cookie
                .body("name", equalTo(name))
                .extract().cookie("SESSION")

        Assert.assertNotEquals(basic, cookie)
        checkAuthenticatedCookie(basic, 200)

        val login = given().contentType(ContentType.JSON)
                .body(UserDto(name, password))
                .post("/login")
                .then()
                .statusCode(204)
                .cookie("SESSION") // new SESSION cookie
                .extract().cookie("SESSION")

        Assert.assertNotEquals(login, cookie)
        Assert.assertNotEquals(login, basic)
        checkAuthenticatedCookie(login, 200)
    }

    @Test
    fun testLogout() {
        val cookie = registerUser("foo", "bar")

        //now, logout will invalidate the cookie
        given().cookie("SESSION", cookie)
                .post("/logout")
                .then()
                .statusCode(204)

        given().cookie("SESSION", cookie)
                .get("/user")
                .then()
                .statusCode(401)
    }

    @Test
    fun testWrongLogin() {

        val name = "foo"
        val password = "bar"

        val noAuth = given().contentType(ContentType.JSON)
                .body(UserDto(name, password))
                .post("/login")
                .then()
                .statusCode(400)
                .extract().cookie("SESSION")

        checkAuthenticatedCookie(noAuth, 401)

        registerUser(name, password)

        val auth = given().contentType(ContentType.JSON)
                .body(UserDto(name, password))
                .post("/login")
                .then()
                .statusCode(204)
                .extract().cookie("SESSION")

        checkAuthenticatedCookie(auth, 200)
    }

    @Test
    fun testRegisterUserWithInvalidUsername() {

         given().contentType(ContentType.JSON)
                .body(UserDto(password = "password"))
                .post("/signUp")
                .then()
                .statusCode(400)
    }
}