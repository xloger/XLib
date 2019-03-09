package com.xloger.xlib.tool

import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.*
import android.support.v4.content.ContextCompat.startActivity



/**
 * Created on 2018/11/13 17:21.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object XNative {
    private lateinit var photoUri : Uri

    fun callPhone(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        XInit.applicationContext?.startActivity(intent) ?: Xlog.e("XInit 没有正确初始化！")
    }

    fun choosePicture(activity: Activity, requestCode: Int, type: String = "image/jpeg") {
        val intent = Intent(Intent.ACTION_GET_CONTENT)//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = type
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            activity.startActivityForResult(intent, requestCode)
        } else {
            activity.startActivityForResult(intent, requestCode)
        }
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


    fun onChooseImageResult(requestCode: Int, resultCode: Int, data: Intent?, context: Context) : String {
        var path: String = ""
        val data1 = data?.data
        if (data1 == null) {
            path = ""
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                path = getPath(context, data1) ?: ""
            } else {
                path = selectImage(context, data1) ?: ""
            }
        }
        return path
    }


    private fun selectImage(context: Context, uri: Uri): String? {
        val uriStr = uri.toString()
        val path = uriStr.substring(10, uriStr.length)
        if (path.startsWith("com.sec.android.gallery3d")) {
            Xlog.debug("It's auto backup pic path:" + uri.toString())
            return null
        }

        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()
        return picturePath
    }

    private fun getPath(context: Context, uri: Uri): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {

            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(docId))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }

        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return if (isGooglePhotosUri(uri)) {
                uri.lastPathSegment
            } else {
                getDataColumn(context, uri, null, null)
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }

        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            } else {
                Xlog.debug("为Null")
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return null
    }

}