package org.bjh.payment.api

import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.dto.PaymentDto
import org.bjh.payment.LocalApplicationRunner
import org.junit.Ignore
import org.junit.Test

class PaymentApiTest : LocalApplicationRunner() {

    @Test
    @Ignore
    fun testCreatePayment() {
        given().contentType(ContentType.JSON)
                .body(PaymentDto(null, "TEST USER", "RANDOM_TOKEN_CREATED_BY_STRIPE", "120.0"))
                .post()
                .then()
                .statusCode(201)
    }

    @Test
    @Ignore
    fun testCreateWithInvalidAuthTokenPayment() {
        given().contentType(ContentType.JSON)
                .body(PaymentDto(null, "TEST USER", null, "120.0"))
                .post()
                .then()
                .statusCode(400)
    }
}