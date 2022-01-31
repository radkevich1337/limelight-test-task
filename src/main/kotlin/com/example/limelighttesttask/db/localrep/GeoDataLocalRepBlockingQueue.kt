package com.example.limelighttesttask.db.localrep

import com.example.limelighttesttask.model.data.GeoData
import com.example.limelighttesttask.model.exception.BadAddToCollectionException
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

class GeoDataLocalRepBlockingQueue: GeoDataLocalRep {
    private val dataRep: BlockingQueue<GeoData> = LinkedBlockingQueue()
    private val tempRep: BlockingQueue<GeoData> = LinkedBlockingQueue()

    override fun putData(geoData: GeoData) {
        if (!dataRep.offer(geoData)) throw BadAddToCollectionException()
    }

    override fun getData(): Collection<GeoData> {
        return tempRep
    }

    override fun update() {
        tempRep.addAll(dataRep)
    }

    override fun clear() {
        dataRep.removeAll(tempRep)
        tempRep.clear()
    }
}