package com.practicum.tracklistmaker.player.domain.repository

import com.practicum.tracklistmaker.player.domain.entities.AudioPlayerStatesListener

interface AudioPlayerRepository {

    fun preparePlayer(trackUrl: String, listener: AudioPlayerStatesListener)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun provideProgressTime(): Int

    fun mediaPlayerIsPlaying(): Boolean

}