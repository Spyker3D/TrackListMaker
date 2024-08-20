package com.practicum.tracklistmaker.search.data.storage

import com.practicum.tracklistmaker.search.data.dto.TrackDto

interface TrackStorage {

    fun  saveHistoryTrackList(historyTrackList: List<TrackDto>)

    fun getHistoryTrackList(): List<TrackDto>

}