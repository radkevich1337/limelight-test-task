package com.example.limelighttesttask.db.localrep

import com.example.limelighttesttask.model.data.GeoData

interface GeoDataLocalRep {
    fun putData(geoData: GeoData)
    fun getData(): Collection<GeoData>
    fun update()
    fun clear()
}