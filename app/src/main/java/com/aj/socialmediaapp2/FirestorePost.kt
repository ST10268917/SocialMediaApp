package com.aj.socialmediaapp2

data class FirestorePost(
    val caption: String = "",
    val imageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)