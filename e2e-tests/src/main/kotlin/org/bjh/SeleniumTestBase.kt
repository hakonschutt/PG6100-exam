package org.bjh

import io.restassured.RestAssured.given
import org.awaitility.Awaitility.await
import org.bjh.po.HomePO
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.testcontainers.containers.DockerComposeContainer
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * From andrea's repository
 */
abstract class SeleniumTestBase {

    protected abstract fun getDriver(): WebDriver

    protected abstract fun getServerHost(): String

    protected abstract fun getServerPort(): Int


    companion object {


        class KDockerComposeContainer(id: String, path: File) : DockerComposeContainer<KDockerComposeContainer>(id, path)

        private const val composeId = "cinema"

        @ClassRule
        @JvmField
        val env: KDockerComposeContainer =
                KDockerComposeContainer(composeId, File("../../docker-compose.yml"))
                        .withLocalCompose(true)


        @BeforeClass
        @JvmStatic
        fun waitForServer() {


            await().atMost(40, TimeUnit.SECONDS)
                    .pollInterval(4, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .until {
                        given().get("http://localhost:80/").then().statusCode(200)
                        true
                    }
        }
    }


    private var home: HomePO? = null

    @Before
    fun initTest() {
        home = HomePO(getDriver(), getServerHost(), getServerPort())

        home!!.toStartingPage()

        assertTrue("Failed to start from Home Page", home!!.isOnPage)
    }


    @Test
    fun testHomePage() {

        val displayed = home!!.waitForVisibility(3, By.id("toMoviesButton"))

        assertTrue(displayed)
    }



}