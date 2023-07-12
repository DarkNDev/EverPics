package com.darkndev.everpics.features

interface Downloader {
    fun downloadFile(url: String): Long
}