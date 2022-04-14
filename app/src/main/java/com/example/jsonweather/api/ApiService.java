package com.example.jsonweather.api;

import com.example.jsonweather.ModalCityQuery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    //https://www.metaweather.com/api/location/search/?query=london
    Gson gson=new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
    ApiService apiService=new Retrofit.Builder().baseUrl("https://www.metaweather.com/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build().create(ApiService.class);
    @GET("api/location/search/")
    Call<List<ModalCityQuery>> callApi(@Query("query")String query);
}
