package com.example.limelighttesttask.db.geodata.dao

import com.example.limelighttesttask.model.data.GeoData
import com.example.limelighttesttask.model.data.StatsData

interface GeoDataDao {
    fun insertBatchGeoData(batchGeoData: Collection<GeoData>)
    fun getCountryStats(startDate: String, endDate: String): List<StatsData>
    fun getGeoData(startDate: String, endDate: String): List<GeoData>
}