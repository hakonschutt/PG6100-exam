package org.bjh.payment.api

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.dto.PaymentDto
import org.bjh.payment.TestBase
import org.junit.Ignore
import org.junit.Test

class PaymentApiTest : TestBase() {

    @Test
    fun testCreatePayment() {
        given().contentType(ContentType.JSON)
                .body(PaymentDto(null, "TEST USER", "RANDOM_TOKEN_CREATED_BY_STRIPE", "120.0"))
                .post()
                .then()
                .statusCode(201)
    }

    @Test
    fun testCreateWithInvalidAuthTokenPayment() {
        given().contentType(ContentType.JSON)
                .body(PaymentDto(null, "TEST USER", null, "120.0"))
                .post()
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidPrice() {
        given().contentType(ContentType.JSON)
                .body(PaymentDto(null, "TEST USER", "RANDOM_TOKEN", "NOT A DOUBLE VALUE"))
                .post()
                .then()
                .statusCode(400)
    }
}