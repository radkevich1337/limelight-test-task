package com.example.limelighttesttask.db.geodata.dao

import com.example.limelighttesttask.model.data.GeoData
import com.example.limelighttesttask.model.data.StatsData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class GeoDataDaoImpl @Autowired constructor(
    private val namedJdbcTemplate: NamedParameterJdbcTemplate
): GeoDataDao {
    private val INSERT_GEO_DATA = "insert into events (timestamp, country, ipAddress, userId) " +
            "values (:timestamp, :country, :ipAddress, :userId);";

    private val GET_COUNTRY_STATS = "select toDate(timestamp) as date, country, count(*) as count " +
            "from events " +
            "where toDate(timestamp) >= :startDate and toDate(timestamp) <= :endDate " +
            "group by country, toDate(timestamp) " +
            "order by toDate(timestamp);"

    private val GET_GEO_DATA = "select * " +
            "from events " +
            "where toDate(timestamp) >= :startDate and toDate(timestamp) <= :endDate " +
            "order by timestamp;"

    override fun insertBatchGeoData(batchGeoData: Collection<GeoData>) {
        val batchValues: Array<MutableMap<String, String>?> = arrayOfNulls(batchGeoData.size)

        batchGeoData.forEachIndexed { index, geoData ->
            val value = mutableMapOf<String, String>()
            value["timestamp"] = geoData.timestamp
            value["country"] = geoData.country
            value["ipAddress"] = geoData.ipAddress
            value["userId"] = geoData.userId
            batchValues[index] = value
        }
        namedJdbcTemplate.batchUpdate(INSERT_GEO_DATA, batchValues)
    }

    override fun getCountryStats(startDate: String, endDate: String): List<StatsData> {
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("startDate", startDate)
        paramSource.addValue("endDate", endDate)

        return  namedJdbcTemplate.query(GET_COUNTRY_STATS, paramSource, RowMapper {
                rs, rowNum -> StatsData(rs.getString("date"),
            rs.getString("country"), rs.getInt("count"))
        })
    }

    override fun getGeoData(startDate: String, endDate: String): List<GeoData> {
        val paramSource = MapSqlParameterSource()
        paramSource.addValue("startDate", startDate)
        paramSource.addValue("endDate", endDate)

        return  namedJdbcTemplate.query(GET_GEO_DATA, paramSource, RowMapper {
                rs, rowNum -> GeoData(rs.getString("country"),
            rs.getString("timestamp"), rs.getString("userId"),
            rs.getString("ipAddress"))
        })
    }
}