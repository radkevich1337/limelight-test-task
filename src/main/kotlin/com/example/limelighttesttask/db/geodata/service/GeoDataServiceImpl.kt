package com.example.limelighttesttask.db.geodata.service

import com.example.limelighttesttask.db.geodata.dao.GeoDataDao
import com.example.limelighttesttask.model.data.GeoData
import com.example.limelighttesttask.model.data.StatsData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GeoDataServiceImpl @Autowired constructor(
    private val geoDataDao: GeoDataDao
): GeoDataService {
    override fun putBatchGeoData(batchGeoData: Collection<GeoData>) {
        geoDataDao.insertBatchGeoData(batchGeoData)
    }

    override fun getCountryStats(startDate: String, endDate: String): List<StatsData> {
        return geoDataDao.getCountryStats(startDate, endDate)
    }

    override fun getCountryStatsLocal(startDate: String, endDate: String):  List<GeoData> {
        return geoDataDao.getGeoData(startDate, endDate)
    }
}