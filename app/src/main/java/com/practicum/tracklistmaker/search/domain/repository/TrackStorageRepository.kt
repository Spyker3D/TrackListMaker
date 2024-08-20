package com.practicum.tracklistmaker.search.domain.repository

import com.practicum.tracklistmaker.search.domain.entities.TrackInfo

interface TrackStorageRepository {

    fun saveHistoryTrackList(historyTrackListToSave: List<TrackInfo>)

    suspend fun getHistoryTrackList(): List<TrackInfo>

}