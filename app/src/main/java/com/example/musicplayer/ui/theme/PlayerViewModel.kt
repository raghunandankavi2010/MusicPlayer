package com.example.musicplayer.ui.theme



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.data.MusicRepository
import com.example.musicplayer.data.Song
import com.example.musicplayer.playback.PlayerController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val playerController: PlayerController,
    private val musicRepository: MusicRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()

    private var progressUpdateJob: Job? = null

    init {
        // initialization moved to initViewModel() to facilitate testing
    }

    fun initViewModel() {
        playerController.initialize()
        loadSongs()
        collectPlayerState()
        startProgressUpdates()
    }

    private fun loadSongs() {
        _uiState.update { it.copy(songs = musicRepository.getPlaylist()) }
    }

    private fun collectPlayerState() {
        viewModelScope.launch {
            playerController.playerState.collect { state ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isPlaying = state.isPlaying,
                        currentSongIndex = state.currentMediaItemIndex,
                        duration = state.duration,
                        currentPosition = state.currentPosition,
                        bufferedPosition = state.bufferedPosition
                    )
                }
            }
        }
    }

    private fun startProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                playerController.updatePosition()
            }
        }
    }

    fun playPause() {
        playerController.playPause()
    }

    fun stop() {
        playerController.stop()
    }

    fun seekTo(position: Long) {
        playerController.seekTo(position)
    }

    fun next() {
        playerController.seekToNext()
    }

    fun previous() {
        playerController.seekToPrevious()
    }

    fun playSongAt(index: Int) {
        playerController.playAtIndex(index)
    }

    override fun onCleared() {
        super.onCleared()
        progressUpdateJob?.cancel()
        playerController.release()
    }

    data class MusicUiState(
        val songs: List<Song> = emptyList(),
        val isPlaying: Boolean = false,
        val currentSongIndex: Int = 0,
        val duration: Long = 0,
        val currentPosition: Long = 0,
        val bufferedPosition: Long = 0
    ) {
        val currentSong: Song?
            get() = songs.getOrNull(currentSongIndex)
    }
}