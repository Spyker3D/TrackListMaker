package com.practicum.tracklistmaker.sharing.interactor

import com.practicum.tracklistmaker.mediaLibrary.domain.entities.Playlist
import com.practicum.tracklistmaker.search.presentation.entities.Track
import com.practicum.tracklistmaker.sharing.domain.externalNavigator.NavigatorRepository

class NavigatorInteractor(private val navigatorRepository: NavigatorRepository) {

    fun shareApp() {
        navigatorRepository.shareApp()
    }

    fun openTerms() {
        navigatorRepository.openTerms()
    }

    fun openEmail() {
        navigatorRepository.openEmail()
    }

    fun sharePlaylist(playlist: Playlist, trackList: List<Track>) {
        navigatorRepository.sharePlaylist(playlist = playlist, trackList = trackList)
    }
}