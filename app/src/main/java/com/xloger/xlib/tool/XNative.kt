package com.xloger.xlib.tool

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created on 2018/11/13 17:21.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object XNative {
    private lateinit var photoUri : Uri

    fun choosePicture() {

    }

    fun chooseFile() {

    }

    fun takePhoto(activity: Activity) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val timeStampFormat = SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss")
        val filename = timeStampFormat.format(Date())
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, filename)

        photoUri = activity.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        activity.startActivityForResult(intent, 1)
    }

}