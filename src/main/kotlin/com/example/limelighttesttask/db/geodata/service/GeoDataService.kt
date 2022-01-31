package com.example.limelighttesttask.db.geodata.service

import com.example.limelighttesttask.model.data.GeoData
import com.example.limelighttesttask.model.data.StatsData

interface GeoDataService {
    fun putBatchGeoData(batchGeoData: Collection<GeoData>)
    fun getCountryStats(startDate: String, endDate: String): List<StatsData>
    fun getCountryStatsLocal(startDate: String, endDate: String): List<GeoData>
}