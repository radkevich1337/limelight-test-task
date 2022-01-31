package com.example.limelighttesttask.model.dto

import lombok.Getter
import javax.validation.constraints.NotBlank

@Getter
data class GeoDataDto(
    @field:NotBlank
    val country: String,
    val timestamp: Long,
    @field:NotBlank
    val userId: String
    )
