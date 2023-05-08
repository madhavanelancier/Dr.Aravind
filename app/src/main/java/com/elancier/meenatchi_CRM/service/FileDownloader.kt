package com.elancier.meenatchi_CRM.service

import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


object FileDownloader {
    private const val TAG = "FileDownloader"
    private const val MEGABYTE = 1024 * 1024
    fun downloadFile(fileUrl: String, directory: File) {
        try {
            Log.v(TAG, "downloadFile() invoked ")
            Log.v(TAG, "downloadFile() fileUrl $fileUrl")
            Log.v(TAG, "downloadFile() directory $directory")
            val url = URL(fileUrl)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            val inputStream = urlConnection.inputStream
            val fileOutputStream = FileOutputStream(directory)
            val totalSize = urlConnection.contentLength
            val buffer = ByteArray(MEGABYTE)
            var bufferLength = 0
            while (inputStream.read(buffer).also { bufferLength = it } > 0) {
                fileOutputStream.write(buffer, 0, bufferLength)
            }
            fileOutputStream.close()
            Log.v(TAG, "downloadFile() completed ")

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.e(TAG, "downloadFile() error" + e.message)
            Log.e(TAG, "downloadFile() error" + e.stackTrace)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            Log.e(TAG, "downloadFile() error" + e.message)
            Log.e(TAG, "downloadFile() error" + e.stackTrace)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e(TAG, "downloadFile() error" + e.message)
            Log.e(TAG, "downloadFile() error" + e.stackTrace)
        }
    }
}