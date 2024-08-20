package com.practicum.tracklistmaker.player.domain.entities

interface AudioPlayerStatesListener {

    fun onPrepared()

    fun onCompletion()

}