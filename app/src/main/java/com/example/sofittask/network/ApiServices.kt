package com.example.sofittask.network

import com.example.sofittask.presentation.ui.model.DrinksContainer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("search.php")
    fun searchDrinksByName(@Query("s") query: String): Call<DrinksContainer>

    @GET("search.php")
    fun searchByFirstLetter(@Query("f") firstLetter: String): Call<DrinksContainer>


}