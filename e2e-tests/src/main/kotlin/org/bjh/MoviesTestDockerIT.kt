package org.bjh

import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.testcontainers.containers.BrowserWebDriverContainer



/**
 * From andrea's repository
 */
@Ignore
class MoviesTestDockerIT : SeleniumTestBase(){

    class KBrowserWebDriverContainer : BrowserWebDriverContainer<KBrowserWebDriverContainer>()

    @Rule
    @JvmField
    val browser: KBrowserWebDriverContainer = KBrowserWebDriverContainer()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withNetworkMode("cinema-network")

    override fun getDriver(): WebDriver {
        return browser.webDriver
    }

    override fun getServerHost(): String {
        return "frontend" // same name as in docker-compose.yml file
    }

    override fun getServerPort(): Int {
        return 8080
    }

    @Test
    fun testGoToHomePageAndClickToGoToMoviePage() {
        is
    }

}