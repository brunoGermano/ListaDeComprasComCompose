package com.bruno.applistadecomprascompose.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter

import java.io.ByteArrayOutputStream

/* O Room não é compatível com "Bitmap", mas sim com "ByteArray" */

class Converters {

    @TypeConverter // usada pra indicar conversão
//    fun fromBitmap( bitmap: Bitmap): ByteArray{
    fun fromBitmap( bitmap: Bitmap?): ByteArray?{
        /*
        val outputStream = ByteArrayOutputStream()
        bitmap.compress( Bitmap.CompressFormat.PNG, 100, outputStream ) // fazendo a compressão
        return outputStream.toByteArray() // fazendo a conversão
        */
        return bitmap?.let{
            val outputStream = ByteArrayOutputStream()
            bitmap.compress( Bitmap.CompressFormat.PNG, 100, outputStream ) // fazendo a compressão
            outputStream.toByteArray() // fazendo a conversão
        }

    }
    @TypeConverter
//    fun toBitmap( bytes: ByteArray ): Bitmap{
    fun toBitmap( bytes: ByteArray? ): Bitmap?{
//        return BitmapFactory.decodeByteArray( bytes, 0, bytes.size )
        return bytes?.let{
            BitmapFactory.decodeByteArray( bytes, 0, bytes.size )
        }
    }

}