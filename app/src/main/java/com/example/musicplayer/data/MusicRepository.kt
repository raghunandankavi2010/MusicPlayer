package com.example.musicplayer.data


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor() {

    // Free open source music from various sources
    fun getPlaylist(): List<Song> = listOf(
        Song(
            id = "1",
            title = "Creative Minds",
            artist = "Benjamin Tissot",
            album = "Royalty Free Music",
            mediaUrl = "https://www.bensound.com/bensound-music/bensound-creativeminds.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500&auto=format&fit=crop&q=60",
            duration = 204000
        ),
        Song(
            id = "2",
            title = "Sunny",
            artist = "Benjamin Tissot",
            album = "Royalty Free Music",
            mediaUrl = "https://www.bensound.com/bensound-music/bensound-sunny.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?w=500&auto=format&fit=crop&q=60",
            duration = 140000
        ),
        Song(
            id = "3",
            title = "Energy",
            artist = "Benjamin Tissot",
            album = "Royalty Free Music",
            mediaUrl = "https://www.bensound.com/bensound-music/bensound-energy.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=500&auto=format&fit=crop&q=60",
            duration = 194000
        ),
        Song(
            id = "4",
            title = "Memories",
            artist = "Benjamin Tissot",
            album = "Royalty Free Music",
            mediaUrl = "https://www.bensound.com/bensound-music/bensound-memories.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1514320291840-2e0a9bf2a9ae?w=500&auto=format&fit=crop&q=60",
            duration = 234000
        ),
        Song(
            id = "5",
            title = "Ukulele",
            artist = "Benjamin Tissot",
            album = "Royalty Free Music",
            mediaUrl = "https://www.bensound.com/bensound-music/bensound-ukulele.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1520454974749-611b7248ffc6?w=500&auto=format&fit=crop&q=60",
            duration = 146000
        ),
        Song(
            id = "6",
            title = "Happy Rock",
            artist = "Benjamin Tissot",
            album = "Royalty Free Music",
            mediaUrl = "https://www.bensound.com/bensound-music/bensound-happyrock.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1498038432885-c6f3f1b912ee?w=500&auto=format&fit=crop&q=60",
            duration = 105000
        ),
        Song(
            id = "7",
            title = "Jazz Comedy",
            artist = "Benjamin Tissot",
            album = "Royalty Free Music",
            mediaUrl = "https://www.bensound.com/bensound-music/bensound-jazzcomedy.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?w=500&auto=format&fit=crop&q=60",
            duration = 194000
        ),
        Song(
            id = "8",
            title = "Little Idea",
            artist = "Benjamin Tissot",
            album = "Royalty Free Music",
            mediaUrl = "https://www.bensound.com/bensound-music/bensound-littleidea.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1507838153414-b4b713384a76?w=500&auto=format&fit=crop&q=60",
            duration = 175000
        )
    )
}