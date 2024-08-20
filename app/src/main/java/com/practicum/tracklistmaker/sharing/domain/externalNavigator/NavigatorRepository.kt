package com.practicum.tracklistmaker.sharing.domain.externalNavigator

import com.practicum.tracklistmaker.mediaLibrary.domain.entities.Playlist
import com.practicum.tracklistmaker.search.presentation.entities.Track


interface NavigatorRepository {

    fun shareApp()

    fun openTerms()

    fun openEmail()

    fun sharePlaylist(playlist: Playlist, trackList: List<Track>)
}