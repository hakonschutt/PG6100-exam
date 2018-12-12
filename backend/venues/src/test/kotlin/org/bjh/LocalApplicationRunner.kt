package org.bjh

import org.springframework.boot.runApplication

class LocalApplicationRunner {
}
fun main(args: Array<String>) {
    runApplication<VenuesApplicationRunner>(*args)

}