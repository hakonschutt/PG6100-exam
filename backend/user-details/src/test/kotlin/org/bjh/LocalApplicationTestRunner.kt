package org.bjh

import io.restassured.RestAssured
import org.bjh.entity.UserDetailEntity
import org.bjh.repository.UserDetailsRepository
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner



@RunWith(SpringRunner::class)
@SpringBootTest(classes = [(UserDetailsRunner::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class LocalApplicationTestRunner {

    private val venueEntities = ArrayList<UserDetailEntity>()
    @Autowired
    protected lateinit var repository: UserDetailsRepository


    @LocalServerPort
    protected var port = 0

    fun prepTestData() {
        venueEntities.removeAll(venueEntities)

        listOf("ola@gmail.com","per@gmail.com","kari@gmail.com")
                .stream()
                .forEach { data ->
                    venueEntities
                            .add(UserDetailEntity(
                                    email=data,
                                    purchaseHistory = setOf()
                            ))
                }
        repository.run {
            deleteAll()
            venueEntities.forEach {
                save(it)
            }
        }
    }

    @Before
    @After
    fun clean() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/users"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        prepTestData()


    }
}
