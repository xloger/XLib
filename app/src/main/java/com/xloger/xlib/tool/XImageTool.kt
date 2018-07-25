package com.xloger.xlib.tool

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

import com.xloger.xlib.tool.Xlog

import java.io.File
import java.io.IOException
import java.util.ArrayList

/**
 * Created by xloger on 9月8日.
 * Author:xloger
 * Email:phoenix@xloger.com
 * 图片处理工具库
 */
object XImageTool {

    /**
     * 根据传入的媒体资源Uri，得到文件的存储路径
     *
     * @param context
     * @param uri
     * @return
     */
    fun getPath(context: Context, uri: Uri): String? {
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

    /**
     * 另一种途径根据 uri 得到真实的存储途径
     */
    fun selectImage(context: Context, uri: Uri?): String? {
        if (uri != null) {
            val uriStr = uri.toString()
            val path = uriStr.substring(10, uriStr.length)
            if (path.startsWith("com.sec.android.gallery3d")) {
                Xlog.debug("It's auto backup pic path:" + uri.toString())
                return null
            }
        }
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri!!, filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()
        return picturePath
    }


    /**
     * 根据传入的origid返回路径
     *
     * @param context
     * @param origId
     * @return
     */
    fun getImgUrl(context: Context, origId: Long): String? {
        return getPath(context, Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                origId.toString() + ""))
    }


    /**
     * 封装后供调用的选择图片方法。需在 Activity 的 onActivityResult 方法内回调 onChooseImageResult 方法
     */
    fun chooseImage(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)//ACTION_OPEN_DOCUMENT
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/jpeg"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            activity.startActivityForResult(intent, 44)
        } else {
            activity.startActivityForResult(intent, 43)
        }
    }

    /**
     * 配合 chooseImage 方法使用，返回图片真实路径。
     */
    fun onChooseImageResult(requestCode: Int, resultCode: Int, data: Intent?, context: Context) : String {
        var path: String = ""
        val data1 = data?.data
        if (data1 == null) {
            path = ""
        } else {
            if (requestCode == 43) {
                path = XImageTool.selectImage(context, data1) ?: ""
            } else if (requestCode == 44) {
                path = XImageTool.getPath(context, data1) ?: ""
            }
        }
        return path
    }


    /**
     * 按输入的宽高对图片进行压缩，返回bitmap。传入参数为0则选另一个作为参考
     *
     * @param path
     * @param toWidth
     * @param toHeight
     * @return
     */
    fun loadScaledBitmap(path: String, toWidth: Int, toHeight: Int): Bitmap? {
        var ret: Bitmap? = null
        //1. 只获取尺寸(使用Options来设置)
        //对象内部的变量会传给底层解码
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)

        val picWidth = options.outWidth
        val picHeight = options.outHeight
        var endSize = 1
        if (toWidth != 0 && toHeight != 0) {
            val wSize = picWidth / toWidth
            val hSize = picHeight / toHeight
            val toSize = Math.max(wSize, hSize)
            val logSize = (Math.log(toSize.toDouble()) / Math.log(2.0)).toInt()
            endSize = Math.pow(2.0, logSize.toDouble()).toInt()
        } else if (toWidth != 0) {
            endSize = picWidth / toWidth
        } else {
            endSize = picHeight / toHeight
        }


        //2. 缩小图片
        options.inJustDecodeBounds = false
        options.inSampleSize = endSize //图片采样比率,2的次方为参数效率最高
        options.inPreferredConfig = Bitmap.Config.RGB_565
        ret = BitmapFactory.decodeFile(path, options)

        Xlog.debug("loadScaledBitmap实际宽度：$picWidth，实际长度：$picHeight，endSize:$endSize")

        return ret
    }


    /**
     * 按输入的倍数进行压缩
     *
     * @param path
     * @param inSampleSize
     * @return
     */
    fun loadScaledBitmap(path: String, inSampleSize: Int): Bitmap? {
        var ret: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = false
        options.inSampleSize = inSampleSize
        options.inPreferredConfig = Bitmap.Config.RGB_565
        ret = BitmapFactory.decodeFile(path, options)

        return ret
    }

    /**
     * 针对图片数据进行转换缩小
     *
     * @param data     实际数据
     * @param toWidth  转换后的宽度
     * @param toHeight 转换后的高度
     * @return 缩小后的Bitmap
     */
    fun loadScaledBitmap(data: ByteArray, toWidth: Int, toHeight: Int): Bitmap? {
        var ret: Bitmap? = null
        //1. 只获取尺寸(使用Options来设置)
        //对象内部的变量会传给底层解码
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(data, 0, data.size, options)

        val picWidth = options.outWidth
        val picHeight = options.outHeight

        val wSize = picWidth / toWidth
        val hSize = picHeight / toHeight
        val toSize = Math.max(wSize, hSize)
        val logSize = (Math.log(toSize.toDouble()) / Math.log(2.0)).toInt()
        val endSize = Math.pow(2.0, logSize.toDouble()).toInt()

        //2. 缩小图片
        options.inJustDecodeBounds = false
        options.inSampleSize = endSize //图片采样比率,2的次方为参数效率最高
        //        options.inPreferredConfig=Bitmap.Config.RGB_565;
        ret = BitmapFactory.decodeByteArray(data, 0, data.size, options)

        Xlog.debug("实际宽度：$picWidth，实际长度：$picHeight,toSize:$toSize，endSize:$endSize")

        return ret
    }

    /**
     * 裁剪为正方形
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    fun cropBitmap(bitmap: Bitmap): Bitmap {
        val w = bitmap.width // 得到图片的宽，高
        val h = bitmap.height
        val cropWidth = if (w >= h) h else w// 裁切后所取的正方形区域边长
        val retX = if (w > h) (w - h) / 2 else 0// 基于原图，取正方形左上角x坐标
        val retY = if (w > h) 0 else (h - w) / 2
        return Bitmap.createBitmap(bitmap, retX, retY, cropWidth, cropWidth, null, false)
    }
}
