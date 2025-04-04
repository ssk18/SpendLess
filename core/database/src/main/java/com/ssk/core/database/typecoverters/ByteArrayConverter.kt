package com.ssk.core.database.typecoverters

import androidx.room.TypeConverter
import java.util.Base64

class ByteArrayConverter {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray?): String? {
        return byteArray?.let { Base64.getEncoder().encodeToString(it) }
    }

    @TypeConverter
    fun toByteArray(string: String?): ByteArray? {
        return string?.let { Base64.getDecoder().decode(it) }
    }
}