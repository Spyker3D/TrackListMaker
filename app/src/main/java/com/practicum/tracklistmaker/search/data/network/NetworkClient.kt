package com.practicum.tracklistmaker.search.data.network

import com.practicum.tracklistmaker.search.data.dto.NetworkResponse

interface NetworkClient {

    suspend fun doRequest(dto: Any?): NetworkResponse
}