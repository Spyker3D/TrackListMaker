package com.practicum.tracklistmaker.player.domain.interactor

import com.practicum.tracklistmaker.player.domain.entities.AudioPlayerStatesListener
import com.practicum.tracklistmaker.player.domain.repository.AudioPlayerRepository

class AudioPlayerInteractor(private val audioPlayerRepository: AudioPlayerRepository) {


    fun preparePlayer(trackUrl: String, listener: AudioPlayerStatesListener) {
        audioPlayerRepository.preparePlayer(trackUrl, listener)
    }

    fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    fun releasePlayer() {
        audioPlayerRepository.releasePlayer()
    }

    fun getProgressTime(): Int {
        return audioPlayerRepository.provideProgressTime()
    }

    fun mediaPlayerIsPlaying(): Boolean {
        return audioPlayerRepository.mediaPlayerIsPlaying()
    }
}

