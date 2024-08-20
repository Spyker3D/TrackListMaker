package com.practicum.tracklistmaker.search.domain.repository

import com.practicum.tracklistmaker.search.domain.entities.Resource
import com.practicum.tracklistmaker.search.domain.entities.TrackInfo
import kotlinx.coroutines.flow.Flow

interface TrackSearchRepository {

    fun searchTrack(request: String): Flow<Resource<List<TrackInfo>?>>

}