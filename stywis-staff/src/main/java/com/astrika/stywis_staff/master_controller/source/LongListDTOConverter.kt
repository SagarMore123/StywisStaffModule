package com.astrika.stywis_staff.master_controller.source

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LongListDTOConverter {

    /**
     * Convert a image list to a Json
     */
    @TypeConverter
    fun fromImageSubCategoryJson(list: ArrayList<Long>?): String {
        if (list == null)
            return ""
        return Gson().toJson(list)
    }

    /**
     * Convert a json to a Image list
     */
    @TypeConverter
    fun toImageSubCategory(s: String): ArrayList<Long>? {
        if (s == null || s == "")
            return null
        val notesType = object : TypeToken<ArrayList<Long>>() {}.type
        return Gson().fromJson<ArrayList<Long>>(s, notesType)
    }
}