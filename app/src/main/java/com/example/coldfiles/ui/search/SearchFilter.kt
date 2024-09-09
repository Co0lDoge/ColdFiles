package com.example.coldfiles.ui.search

sealed interface SearchFilter {

    // Value to display
    val name: String

    data object Image : SearchFilter {
        override val name = "Image"
    }

    data object Audio : SearchFilter {
        override val name = "Audio"
    }

    data object Document : SearchFilter {
        override val name = "Document"
    }

    data object InstallationFile: SearchFilter {
        override val name = "Installation File"
    }
}