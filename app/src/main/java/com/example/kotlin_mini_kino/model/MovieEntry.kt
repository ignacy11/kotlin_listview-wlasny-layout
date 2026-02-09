package com.example.kotlin_mini_kino.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieEntry(
    val title: String,
    val genre: String,
    val rating: Int
)