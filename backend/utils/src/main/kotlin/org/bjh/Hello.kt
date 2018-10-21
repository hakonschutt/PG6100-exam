package org.bjh

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.bjh.dataManipulators.JsonManipulator
import java.io.File
import java.io.InputStream

fun main(args: Array<String>) {

    val jsTesting = JsonManipulator()
    val inputStream: InputStream = (File("data/movies.json").inputStream())

    val json = JsonParser().parse(inputStream.bufferedReader().use { it.readText() })

    println(json)

    val test =jsTesting.addPropertyWithValuesToListOfJson(json,"results","price", "200")
    jsTesting.changePropertyOnJsonObject(test[0] as JsonObject,"date","xx")
}

