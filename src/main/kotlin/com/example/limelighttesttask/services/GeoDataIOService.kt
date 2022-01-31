package com.example.limelighttesttask.services

import com.clickhouse.client.internal.google.gson.Gson
import com.example.limelighttesttask.db.geodata.service.GeoDataService
import com.example.limelighttesttask.db.localrep.GeoDataLocalRep
import com.example.limelighttesttask.db.localrep.GeoDataLocalRepBlockingQueue
import com.example.limelighttesttask.model.data.GeoData
import com.example.limelighttesttask.model.data.StatsData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class GeoDataIOService @Autowired constructor(
    private val geoDataService: GeoDataService,
) {
    private val geoDataLocalRep: GeoDataLocalRep = GeoDataLocalRepBlockingQueue()
    private val gson: Gson = Gson()

    init {
        Runtime.getRuntime().addShutdownHook(Thread { putBatchGeoData() })
    }

    @Scheduled(fixedRate = 60 * 1000)
    fun putBatchGeoData() {
        geoDataLocalRep.update()
        if (geoDataLocalRep.getData().isNotEmpty()) {
            geoDataService.putBatchGeoData(geoDataLocalRep.getData())
            geoDataLocalRep.clear()
        }
    }

    fun putGeoData(geoData: GeoData) {
        geoDataLocalRep.putData(geoData)
    }

    fun getCountryStats(startDate: String, endData: String, groupLocal: Boolean): String {
        val result: List<StatsData>
        if (groupLocal) {
            result = geoDataService.getCountryStatsLocal(startDate, endData).
            groupingBy { Pair(it.timestamp.split(" ")[0], it.country)}.
            eachCount().
            map {StatsData(it.key.first, it.key.second, it.value)}
        } else {
            result = geoDataService.getCountryStats(startDate, endData)
        }
        return gson.toJson(result)
    }
}