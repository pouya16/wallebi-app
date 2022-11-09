package com.example.wallebi_app.api.countries;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetCountriesApi {
    @GET("v0/GeneralService/countries/")
    Call<CountriesResponse> getCountries();

}
