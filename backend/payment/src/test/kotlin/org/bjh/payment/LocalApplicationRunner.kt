package org.bjh.payment

import io.restassured.RestAssured
import org.bjh.payment.repository.PaymentRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(PaymentApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class LocalApplicationRunner {

    @LocalServerPort
    protected var port = 0


    @Autowired
    protected lateinit var repository: PaymentRepository

    @Before
    @After
    fun clean() {

        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/payments"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        repository.run {
            deleteAll()
        }
    }
}
