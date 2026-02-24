package com.example.musicplayer

import app.cash.turbine.test
import com.example.musicplayer.data.MusicRepository
import com.example.musicplayer.data.Song
import com.example.musicplayer.playback.PlayerController
import com.example.musicplayer.ui.theme.PlayerViewModel
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PlayerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // Mocks
    private lateinit var playerController: PlayerController
    private lateinit var musicRepository: MusicRepository

    // Class under test
    private lateinit var viewModel: PlayerViewModel

    // Controllable state for the mock player controller
    private val playerStateFlow = MutableStateFlow(PlayerController.PlayerState())

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        playerController = mockk {
            every { initialize() } just runs
            every { release() } just runs
            every { playerState } returns playerStateFlow
            every { updatePosition() } just runs
            every { playPause() } just runs
            every { stop() } just runs
            every { seekTo(any()) } just runs
            every { seekToNext() } just runs
            every { seekToPrevious() } just runs
            every { playAtIndex(any()) } just runs
        }
        musicRepository = mockk()

        viewModel = PlayerViewModel(playerController, musicRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initViewModel initializes controller and loads songs`() = runTest {
        // Arrange
        val songs = listOf(Song("1", "Title", "Artist", "Album", "url", "art"))
        every { musicRepository.getPlaylist() } returns songs

        // Act
        viewModel.initViewModel()

        // Assert
        verify { playerController.initialize() }
        verify { musicRepository.getPlaylist() }

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(songs, state.songs)
        }
    }

    @Test
    fun `uiState updates when player state changes`() = runTest {
        // Arrange
        every { musicRepository.getPlaylist() } returns emptyList()
        viewModel.initViewModel()

        viewModel.uiState.test {
            // Initial state
            assertEquals(false, awaitItem().isPlaying)

            // Act: Simulate player starting to play
            playerStateFlow.value = PlayerController.PlayerState(isPlaying = true, duration = 1000L)

            // Assert: UI state reflects the change
            val updatedState = awaitItem()
            assertEquals(true, updatedState.isPlaying)
            assertEquals(1000L, updatedState.duration)
        }
    }

    @Test
    fun `playPause delegates call to playerController`() {
        // Act
        viewModel.playPause()

        // Assert
        verify { playerController.playPause() }
    }

    @Test
    fun `next delegates call to playerController`() {
        // Act
        viewModel.next()

        // Assert
        verify { playerController.seekToNext() }
    }

    @Test
    fun `previous delegates call to playerController`() {
        // Act
        viewModel.previous()

        // Assert
        verify { playerController.seekToPrevious() }
    }

    @Test
    fun `seekTo delegates call to playerController`() {
        // Arrange
        val position = 12345L

        // Act
        viewModel.seekTo(position)

        // Assert
        verify { playerController.seekTo(position) }
    }

    @Test
    fun `playSongAt delegates call to playerController`() {
        // Arrange
        val index = 3

        // Act
        viewModel.playSongAt(index)

        // Assert
        verify { playerController.playAtIndex(index) }
    }

    @Test
    fun `onCleared releases playerController`() {
        // Act
        viewModel.releaseResources()

        // Assert
        verify { playerController.release() }
    }
}