package com.example.coldfiles.ui.storage

import android.content.Context
import android.content.Intent
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
    context.startActivity(intent)
}