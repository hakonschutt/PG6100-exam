package org.bjh.dataManipulators

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class JsonManipulator (){

    // Method to add / change properties for a json array. Only accepts strings as new values
    fun addPropertyWithValuesToListOfJson(jsonToManipulate:JsonElement,arrayName:String = "results",property:String ="NaN",value:String ="200"): JsonArray {
        if (property.equals("NaN")) return JsonArray()
        val jsonArr:JsonArray  =  ((jsonToManipulate as JsonObject).get(arrayName) )as JsonArray
         jsonArr.forEach { (it as JsonObject).addProperty(property,value) }

        return jsonArr
    }
    // add or edit a single property on a jsonobject, only accepts string values
    fun changePropertyOnJsonObject(jsonObject:JsonObject,propertyToChange:String,value:String) = jsonObject.addProperty(propertyToChange,value)

}