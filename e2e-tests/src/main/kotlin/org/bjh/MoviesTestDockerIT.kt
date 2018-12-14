package org.bjh

import junit.framework.Assert.assertTrue
import org.bjh.po.HomePO
import org.bjh.po.MoviesPO
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.testcontainers.containers.BrowserWebDriverContainer


/**
 * From andrea's repository
 */
@Ignore
class MoviesTestDockerIT : SeleniumTestBase() {

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
        return 80
    }

    @Test
    fun testGoToHomePageAndClickToGoToMoviePage() {
        val home = HomePO(getDriver(), "localhost", getServerPort())
        val movies = MoviesPO(getDriver(), getServerHost(), getServerPort())

        assertTrue(home.isOnPage)
        val goToMoviesButton = getDriver().findElement(By.xpath("toMoviesButton"))
        goToMoviesButton.click()


        val displayed = movies.waitForVisibility(3, By.id("toMoviesButton"))
        assertTrue(movies.isOnPage)
        assertTrue(displayed)

    }

    @Test
    fun testGoToHomePageAndClickToGoToMoviePageAndSeeIfMovies() {
        val home = HomePO(getDriver(), "localhost", getServerPort())
        val movies = MoviesPO(getDriver(), getServerHost(), getServerPort())

        assertTrue(home.isOnPage)
        val goToMoviesButton = getDriver().findElement(By.xpath("toMoviesButton"))
        goToMoviesButton.click()


        val displayed = movies.waitForVisibility(3, By.id("toMoviesButton"))
        assertTrue(movies.isOnPage)
        assertTrue(displayed)
        val moviesCount = getDriver().findElements(By.xpath("//*[@id=“root”]/div/main/div/div/div[2]/div/div/div"))


        assertTrue(moviesCount.size > 0)
    }
}