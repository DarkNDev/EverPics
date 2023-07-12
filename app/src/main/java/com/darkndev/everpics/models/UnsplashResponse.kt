package com.darkndev.everpics.models

data class UnsplashResponse(
    val results: List<UnsplashPhoto>,
    val total: Int
)