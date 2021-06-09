package com.astrika.stywis_staff.utils

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializer
import org.json.JSONObject
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CustomGsonBuilder {


    fun getInstance(): GsonBuilder {
        val ser: JsonSerializer<Date> =
            JsonSerializer { src, typeOfSrc, context ->
                (if (src == null) null else JsonPrimitive(
                    src.time
                ))!!
            }
        val desr: JsonDeserializer<Date?> =
            JsonDeserializer { json, typeOfT, context ->
                if (json.isJsonObject) {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(json.asJsonObject.toString())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (json == null) null else Date(jsonObject!!.optLong("millis"))
                } else {
                    if ((json as JsonPrimitive).isString) {
                        if (json == null) null else getDate(json.getAsString())
                    } else {
                        if (json == null) null else Date(json.getAsLong())
                    }
                }
            }
        return GsonBuilder()
            .registerTypeAdapter(Date::class.java, ser)
            .registerTypeAdapter(Date::class.java, desr)
    }

    fun <T> fromJson(jsonData: String?, type: Class<T>?): T {
        val gson = getInstance().create()
        return gson.fromJson(jsonData, type)
    }

    fun <T> fromJsonList(jsonData: String?, type: Type?): T {
        val gson = getInstance().create()
        return gson.fromJson(jsonData, type)
    }

    private fun getDate(dtStart: String): Date? {
        val format =
            SimpleDateFormat("MMM dd, yyyy HH:mm:ss a")
        return try {
            format.parse(dtStart)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }


}