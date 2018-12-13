package org.bjh

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.bjh.dto.UserDto
import org.bjh.repository.AuthenticationRepository
import org.hamcrest.CoreMatchers
import org.junit.Before
import org.junit.ClassRule
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.GenericContainer


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(AuthenticationApplication::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = [(TestBase.Companion.Initializer::class)])
abstract class TestBase {

    @Autowired
    protected lateinit var repository: AuthenticationRepository

    @LocalServerPort
    protected var port = 0

    /**
     * From Andrea's repository
     */
    companion object {

        class KGenericContainer(imageName: String) : GenericContainer<KGenericContainer>(imageName)

        /*
            Here, going to use an actual Redis instance started in Docker
         */

        @ClassRule
        @JvmField
        val redis = KGenericContainer("redis:latest")
                .withExposedPorts(6379)

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
            override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {

                val host = redis.containerIpAddress
                val port = redis.getMappedPort(6379)

                TestPropertyValues
                        .of("spring.redis.host=$host", "spring.redis.port=$port")
                        .applyTo(configurableApplicationContext.environment)
            }
        }
    }

    /**
     * Util method from Andrea's repository
     */
    fun registerUser(id: String, password: String): String {

        return given().contentType(ContentType.JSON)
                .body(UserDto(id, password))
                .post("/signUp")
                .then()
                .statusCode(204)
                .header("Set-Cookie", CoreMatchers.not(CoreMatchers.equalTo(null)))
                .extract().cookie("SESSION")
    }

    /**
     * Util method from Andrea's repository
     */
    fun checkAuthenticatedCookie(cookie: String, expectedCode: Int){
        given().cookie("SESSION", cookie)
                .get("/user")
                .then()
                .statusCode(expectedCode)
    }

    @Before
    fun clean() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        repository.deleteAll()
    }


}
