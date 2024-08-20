package com.practicum.tracklistmaker.search.data.repository

import com.practicum.tracklistmaker.search.data.mapper.TrackMapper.mapToDomain
import com.practicum.tracklistmaker.search.data.mapper.TrackMapper.mapToStorage
import com.practicum.tracklistmaker.search.data.storage.TrackStorage
import com.practicum.tracklistmaker.search.domain.entities.TrackInfo
import com.practicum.tracklistmaker.search.domain.repository.TrackStorageRepository

class TrackStorageRepositoryImpl(
    private val trackStorage: TrackStorage,
) : TrackStorageRepository {
    override fun saveHistoryTrackList(historyTrackListToSave: List<TrackInfo>) {
        val historyTrackListToSaveMapped = historyTrackListToSave.map { it.mapToStorage() }
        trackStorage.saveHistoryTrackList(historyTrackListToSaveMapped)
    }

    override suspend fun getHistoryTrackList(): List<TrackInfo> {
        val historyTrackListToGet = trackStorage.getHistoryTrackList()
        val historyTrackListTrackInfo = historyTrackListToGet.map { it.mapToDomain() }
        return historyTrackListTrackInfo
    }
}