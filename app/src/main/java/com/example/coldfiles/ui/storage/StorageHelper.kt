package com.example.coldfiles.ui.storage

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File

// Function that opens file depending on it's extension (Not all extensions supported yet)
// TODO: Add support for apk files
fun openFile(file: File, context: Context) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName + ".provider",
        file
    )
    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
    try {
        context.startActivity(intent)
    }
    catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Cannot find application for this file", Toast.LENGTH_SHORT)
            .show()
    }
}

// Function that shares list of files to other apps
// TODO: set intent type based on file type
// TODO: add support for sharing files by recursively adding it's content
fun shareFiles(files: List<File>, context: Context) {
    val intent = Intent()
    intent.setAction(Intent.ACTION_SEND_MULTIPLE)
    intent.putExtra(Intent.EXTRA_SUBJECT, "Files from ColdFiles")
    intent.setType("text/plain")

    val uriList: ArrayList<Uri> = ArrayList()
    for (file in files) {
        val uri = FileProvider.getUriForFile(
            context, context.applicationContext.packageName + ".provider",
            file
        )
        uriList.add(uri)
    }

    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList)

    try {
        context.startActivity(intent)
    }
    catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "Cannot find application to share these files", Toast.LENGTH_SHORT)
            .show()
    }
}