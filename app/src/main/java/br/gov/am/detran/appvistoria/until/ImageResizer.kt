package br.gov.am.detran.appvistoria.until

import android.graphics.Bitmap
import android.util.Log

object ImageResizer {
    //For Image Size 640*480, use MAX_SIZE =  307200 as 640*480 307200
    //private static long MAX_SIZE = 360000;
    //private static long THUMB_SIZE = 6553;
    fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int): Bitmap {
        val ratioSquare: Double
        val bitmapHeight: Int
        val bitmapWidth: Int
        bitmapHeight = bitmap.height
        bitmapWidth = bitmap.width
        ratioSquare = bitmapHeight * bitmapWidth / MAX_SIZE.toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = Math.sqrt(ratioSquare)
        Log.d("mylog", "Ratio: $ratio")
        val requiredHeight = Math.round(bitmapHeight / ratio).toInt()
        val requiredWidth = Math.round(bitmapWidth / ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }

    fun generateThumb(bitmap: Bitmap, THUMB_SIZE: Int): Bitmap {
        val ratioSquare: Double
        val bitmapHeight: Int
        val bitmapWidth: Int
        bitmapHeight = bitmap.height
        bitmapWidth = bitmap.width
        ratioSquare = bitmapHeight * bitmapWidth / THUMB_SIZE.toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = Math.sqrt(ratioSquare)
        Log.d("mylog", "Ratio: $ratio")
        val requiredHeight = Math.round(bitmapHeight / ratio).toInt()
        val requiredWidth = Math.round(bitmapWidth / ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }
}