package com.example.limelighttesttask

import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@SpringBootTest
class EndpointTests: AbstractTest() {
    private val GEO_DATA_ONE: String = "{\"country\": \"UK\", \"timestamp\": 1607427370, \"userId\": \"abs\"}"
    private val GEO_DATA_TWO: String = "{\"country\": \"UK\", \"timestamp\": -1, \"userId\": \"abs\"}"
    private val GEO_DATA_THREE: String = "{\"country\": \"UK\", \"timestamp\": 1607427370}"
    private val GEO_DATA_FOUR: String = "{\"country\": \"RUS\", \"timestamp\": 1607427370, \"userId\": \"abs\"}"

    @Test
    fun endpointGeoData() {
        var result = mockMvc.perform(post("/geodata").
        contentType(MediaType.APPLICATION_JSON).
        content(GEO_DATA_ONE)).andReturn()
        Assertions.assertEquals(result.response.status.toLong(), HttpStatus.OK.value().toLong())

        result = mockMvc.perform(post("/geodata").
        contentType(MediaType.APPLICATION_JSON).
        content(GEO_DATA_TWO)).andReturn()
        Assertions.assertEquals(result.response.status.toLong(), HttpStatus.BAD_REQUEST.value().toLong())

        result = mockMvc.perform(post("/geodata").
        contentType(MediaType.APPLICATION_JSON).
        content(GEO_DATA_THREE)).andReturn()
        Assertions.assertEquals(result.response.status.toLong(), HttpStatus.BAD_REQUEST.value().toLong())
    }

    @Test
    fun endpointCountryStats() {
        mockMvc.perform(post("/geodata").
        contentType(MediaType.APPLICATION_JSON).
        content(GEO_DATA_ONE)).andReturn()

        mockMvc.perform(post("/geodata").
        contentType(MediaType.APPLICATION_JSON).
        content(GEO_DATA_ONE)).andReturn()

        mockMvc.perform(post("/geodata").
        contentType(MediaType.APPLICATION_JSON).
        content(GEO_DATA_FOUR)).andReturn()

        val result1 =  mockMvc.perform(get("/countrystats").
        param("startDate", "2000-01-01").
        param("endDate", "2099-01-01").
        contentType(MediaType.APPLICATION_JSON)).andReturn()
        Assertions.assertEquals(result1.response.status.toLong(), HttpStatus.OK.value().toLong())

        val result2 =  mockMvc.perform(get("/countrystats").
        param("startDate", "2000-01-01").
        param("endDate", "2099-01-01").
        param("groupLocal", "true").
        contentType(MediaType.APPLICATION_JSON)).andReturn()
        Assertions.assertEquals(result2.response.status.toLong(), HttpStatus.OK.value().toLong())

        Assertions.assertTrue(result1.response.contentAsString.toList().sorted() == result2.response.contentAsString.toList().sorted())

        var result =  mockMvc.perform(get("/countrystats").
        param("startDate", "1975-01-01").
        param("endDate", "1976-01-01").
        param("groupLocal", "true").
        contentType(MediaType.APPLICATION_JSON)).andReturn()
        Assertions.assertEquals(result.response.status.toLong(), HttpStatus.NO_CONTENT.value().toLong())

        result =  mockMvc.perform(get("/countrystats").
        param("startDate", "1905-01-01").
        param("endDate", "1906-01-01").
        contentType(MediaType.APPLICATION_JSON)).andReturn()
        Assertions.assertEquals(result.response.status.toLong(), HttpStatus.BAD_REQUEST.value().toLong())

        result =  mockMvc.perform(get("/countrystats").
        param("startDate", "2005-01-01").
        param("endDate", "1906-01-01").
        contentType(MediaType.APPLICATION_JSON)).andReturn()
        Assertions.assertEquals(result.response.status.toLong(), HttpStatus.BAD_REQUEST.value().toLong())
    }
}