package com.example.wallebi_app.api.countries

data class CountriesResponse(
    val success:Int,
    val msg:List<CountriesModel>,
    val err:String
)
