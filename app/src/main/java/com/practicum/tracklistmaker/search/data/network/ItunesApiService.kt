package com.practicum.tracklistmaker.search.data.network

import com.practicum.tracklistmaker.search.data.dto.TracksSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {
    @GET("/search?entity=song")
    suspend fun searchTrack(@Query("term") text: String): TracksSearchResponse
}