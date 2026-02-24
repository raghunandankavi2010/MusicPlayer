package com.example.musicplayer.data


import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor() {

    // Using more reliable sample media URLs to avoid 403 errors
    fun getPlaylist(): List<Song> = listOf(
        Song(
            id = "1",
            title = "Jazz in Paris",
            artist = "Media Right Productions",
            album = "YouTube Audio Library",
            mediaUrl = "https://storage.googleapis.com/exoplayer-test-media-0/Jazz_In_Paris.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500&auto=format&fit=crop&q=60",
            duration = 103000
        ),
        Song(
            id = "2",
            title = "The Big Bang Theory",
            artist = "Barenaked Ladies",
            album = "TV Themes",
            mediaUrl = "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1493225255756-d9584f8606e9?w=500&auto=format&fit=crop&q=60",
            duration = 30000
        ),
        Song(
            id = "3",
            title = "A Thousand Years",
            artist = "Christina Perri",
            album = "Twilight Saga",
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1511671782779-c97d3d27a1d4?w=500&auto=format&fit=crop&q=60",
            duration = 372000
        ),
        Song(
            id = "4",
            title = "Comfortable",
            artist = "John Mayer",
            album = "Any Given Thursday",
            mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            artworkUrl = "https://images.unsplash.com/photo-1514320291840-2e0a9bf2a9ae?w=500&auto=format&fit=crop&q=60",
            duration = 425000
        )
    )
}
