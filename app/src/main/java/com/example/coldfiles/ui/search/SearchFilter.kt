package com.example.coldfiles.ui.search

/** Filters used for searching **/
enum class SearchFilter(
    val displayName: String,
    val extensions: List<String>
) {
    Image("Image", listOf("png", "jpg")),
    Audio("Audio", listOf("mp3")),
    Document("Document", listOf("pdf", "docx")),
    InstallationFile("Installation File", listOf("apk"))
}