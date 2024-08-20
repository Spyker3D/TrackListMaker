package com.practicum.tracklistmaker.search.domain.interactor

import com.practicum.tracklistmaker.search.domain.entities.TrackInfo
import com.practicum.tracklistmaker.search.domain.repository.TrackStorageRepository

class GetHistoryTrackUseCase(private val trackStorage: TrackStorageRepository) {

    suspend fun execute(): List<TrackInfo> {
        return trackStorage.getHistoryTrackList()
    }
}