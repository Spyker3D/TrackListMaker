package com.practicum.tracklistmaker.mediaLibrary.presentation.playlists

import com.practicum.tracklistmaker.mediaLibrary.domain.entities.Playlist

sealed interface PlaylistsState {
    data class Content(val playlistsList: List<Playlist>) : PlaylistsState

    data object Empty : PlaylistsState
}
