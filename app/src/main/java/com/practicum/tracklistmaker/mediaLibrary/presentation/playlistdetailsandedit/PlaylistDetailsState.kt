package com.practicum.tracklistmaker.mediaLibrary.presentation.playlistdetailsandedit

import com.practicum.tracklistmaker.mediaLibrary.domain.entities.Playlist
import com.practicum.tracklistmaker.search.presentation.entities.Track

sealed interface PlaylistDetailsState {

    data class Content(
        val playlist: Playlist,
        val tracksList: List<Track>,
        val playlistLength: Int,
    ) :
        PlaylistDetailsState

    data object Empty : PlaylistDetailsState
}