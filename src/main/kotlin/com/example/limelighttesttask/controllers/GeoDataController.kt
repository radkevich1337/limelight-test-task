package com.example.limelighttesttask.controllers

import com.example.limelighttesttask.model.data.GeoData
import com.example.limelighttesttask.model.dto.GeoDataDto
import com.example.limelighttesttask.model.exception.BadAddToCollectionException
import com.example.limelighttesttask.model.exception.BadTimestampException
import com.example.limelighttesttask.services.GeoDataIOService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.InetAddress
import java.sql.Timestamp
import java.text.SimpleDateFormat
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@RestController
@Validated
class GeoDataController @Autowired constructor(
    private val geoDataIOService: GeoDataIOService
) {

    @PostMapping("/geodata")
    fun putGeoData(request: HttpServletRequest, @RequestBody geoData: @Valid GeoDataDto) : ResponseEntity<String> {
        if (geoData.timestamp < 0) throw BadTimestampException()
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = sdf.format(Timestamp(geoData.timestamp * 1000).time)

        var ip = request.remoteAddr
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            val inetAddress = InetAddress.getLocalHost()
            ip = inetAddress.hostAddress
        }

        geoDataIOService.putGeoData(GeoData(geoData.country, date, geoData.userId, ip))
        return ResponseEntity(HttpStatus.OK)
    }

    @GetMapping("/countrystats")
    fun getCountryStats(@NotBlank @Pattern(regexp = "(19[7-9][0-9]|20[0-9][0-9])-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])") startDate: String,
                        @NotBlank @Pattern(regexp = "(19[7-9][0-9]|20[0-9][0-9])-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])") endDate: String,
                        groupLocal: Boolean = false
    ) : ResponseEntity<String> {
        if (startDate > endDate) { throw ConstraintViolationException(null) }

        val responseEntity: ResponseEntity<String>
        val jsonData = geoDataIOService.getCountryStats(startDate, endDate, groupLocal)

        if (jsonData == "[]") responseEntity = ResponseEntity(HttpStatus.NO_CONTENT)
        else responseEntity = ResponseEntity(jsonData, HttpStatus.OK)

        return responseEntity
    }

    @ExceptionHandler(value = [ConstraintViolationException::class,
        BadTimestampException::class, BadAddToCollectionException::class])
    fun handlerDateException(): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.BAD_REQUEST)
    }
}