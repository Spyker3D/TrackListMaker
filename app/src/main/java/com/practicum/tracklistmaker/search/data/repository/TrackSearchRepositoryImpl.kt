package com.practicum.tracklistmaker.search.data.repository

import com.practicum.tracklistmaker.mediaLibrary.data.db.AppDatabase
import com.practicum.tracklistmaker.search.data.dto.TrackSearchRequest
import com.practicum.tracklistmaker.search.data.dto.TracksSearchResponse
import com.practicum.tracklistmaker.search.data.mapper.TrackMapper.mapToDomain
import com.practicum.tracklistmaker.search.data.network.NetworkClient
import com.practicum.tracklistmaker.search.domain.entities.Resource
import com.practicum.tracklistmaker.search.domain.entities.TrackInfo
import com.practicum.tracklistmaker.search.domain.repository.TrackSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackSearchRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,
) : TrackSearchRepository {
    override fun searchTrack(request: String): Flow<Resource<List<TrackInfo>?>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(request))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.InternetConnectionError())
            }

            200 -> {
                val trackList: List<TrackInfo> =
                    (response as TracksSearchResponse).results.map { it.mapToDomain() }
                emit(Resource.Success(data = trackList))

            }

            else -> emit(Resource.Error())
        }
    }

}

