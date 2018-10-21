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

    val test =jsTesting.addPropertyWithValuesToListOfJson(json,"results","price", "200")
    jsTesting.changePropertyOnJsonObject(test[0] as JsonObject,"date","xx")

    val csvStringyfied = File("data/genres.csv").inputStream().bufferedReader().use { it.readText() }

    val splittedKeyValues = csvStringyfied.substring(8).trim().split(",",System.lineSeparator())
    println(splittedKeyValues)

    val mapOfCsv = HashMap<Int,String>()
    for(i in 0..splittedKeyValues.size-1){
        if(i.rem(2) == 0){
            mapOfCsv[splittedKeyValues[i].trim().toInt()] = splittedKeyValues[i + 1]
        }
    }
    println(mapOfCsv)


    jsTesting.arrayModification(json,"results","genre_ids", mapOfCsv
    )
}

