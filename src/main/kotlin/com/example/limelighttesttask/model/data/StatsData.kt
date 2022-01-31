package com.example.limelighttesttask.model.data

import lombok.Getter

@Getter
data class StatsData(
    val date: String,
    val country: String,
    val count: Int
)
