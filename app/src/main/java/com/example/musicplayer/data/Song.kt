package com.example.musicplayer.data

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val mediaUrl: String,
    val artworkUrl: String,
    val duration: Long = 0
)