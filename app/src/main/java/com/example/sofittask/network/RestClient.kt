package com.example.sofittask.network

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RestClient @Inject constructor(private val apiServices: ApiServices) {
    fun getApiService() = apiServices
}
