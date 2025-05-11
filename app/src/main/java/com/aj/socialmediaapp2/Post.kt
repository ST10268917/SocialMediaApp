package com.aj.socialmediaapp2

import android.net.Uri

data class Post(
    val caption: String,
    val imageUri: Uri,
    var likeCount: Int = 0,
    val comments: MutableList<String> = mutableListOf()
)
