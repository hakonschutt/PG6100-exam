package org.bjh

import kotlin.jvm.JvmStatic
import org.junit.AfterClass
import org.junit.AssumptionViolatedException
import org.junit.BeforeClass
import org.junit.Ignore
import org.openqa.selenium.WebDriver
import org.tsdes.misc.testutils.selenium.SeleniumDriverHandler


/**
 * From andrea's repository
 */
@Ignore
class SpaRestLocalChromeDockerIT : SpaRestSeleniumTestBase() {

    companion object {

        var driver: WebDriver? = null

        @BeforeClass
        @JvmStatic
        fun initClass() {

            driver = SeleniumDriverHandler.getChromeDriver()

            if (driver == null) {
                //Do not fail the tests.
                throw AssumptionViolatedException("Cannot find/initialize Chrome driver")
            }
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            if (driver != null) {
                driver!!.close()
            }
        }
    }

    override fun getDriver(): WebDriver {
        return driver!!
    }

    override fun getServerHost(): String {
        return "localhost"
    }

    override fun getServerPort(): Int {
        return 8080
    }

}