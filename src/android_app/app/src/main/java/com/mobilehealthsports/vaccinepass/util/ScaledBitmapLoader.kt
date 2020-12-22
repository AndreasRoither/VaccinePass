package com.mobilehealthsports.vaccinepass.util

import android.graphics.BitmapFactory
import com.google.android.material.imageview.ShapeableImageView

object ScaledBitmapLoader {
    fun setPic(photoFilePath: String, targetWidth: Int, targetHeight: Int, target: ShapeableImageView) {
        val bmOptions = BitmapFactory.Options()

        bmOptions.apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            BitmapFactory.decodeFile(photoFilePath, bmOptions)

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.max(1, Math.min(photoW / targetWidth, photoH / targetHeight))

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }

        BitmapFactory.decodeFile(photoFilePath, bmOptions)?.also { bitmap ->
            target.setImageBitmap(bitmap)
        }
    }
}