package com.practicum.tracklistmaker.search.domain.consumer

interface Consumer<T> {

    fun consume(data: ConsumerData<T>)
}