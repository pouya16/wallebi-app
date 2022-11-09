package com.example.wallebi_app.api.countries

data class CountriesModel(
    val id:Int,
    val name:String,
    val persian_name:String,
    val full_name:String,
    val flag:String,
    val num_length:Int,
    val area_code:String
)
