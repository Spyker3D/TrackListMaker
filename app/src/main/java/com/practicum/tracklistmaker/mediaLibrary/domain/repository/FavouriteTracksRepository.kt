package com.practicum.tracklistmaker.mediaLibrary.domain.repository

import com.practicum.tracklistmaker.search.domain.entities.TrackInfo
import kotlinx.coroutines.flow.Flow

interface FavouriteTracksRepository {
    suspend fun insertToFavouriteTracks(track: TrackInfo)

    suspend fun deleteFromFavouriteTracks(track: TrackInfo)

    fun getFavouriteTracks(): Flow<List<TrackInfo>>

    suspend fun updateTrackStatus(track: TrackInfo)

    suspend fun isFavourite(track: TrackInfo): Boolean

}
