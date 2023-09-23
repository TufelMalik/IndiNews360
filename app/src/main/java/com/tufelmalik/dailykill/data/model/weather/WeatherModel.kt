package com.tufelmalik.dailykill.data.model.weather

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "weather")
data class WeatherModel(
    @PrimaryKey
    val id: Int,
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)