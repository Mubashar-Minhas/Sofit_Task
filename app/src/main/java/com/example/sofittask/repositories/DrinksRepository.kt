package com.example.sofittask.repositories

import androidx.lifecycle.MutableLiveData
import com.example.sofittask.network.ApiServices
import com.example.sofittask.presentation.ui.interfaces.NetworkResponseCallback
import com.example.sofittask.presentation.ui.model.DrinksContainer
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrinksRepository @Inject constructor(private val apiServices: ApiServices) {
    private var mCallback: NetworkResponseCallback? = null
    private val mDrinksList = MutableLiveData<List<DrinksDataModel>>()

    fun getDrinksByFirstLetter(
        callback: NetworkResponseCallback,
        firstLetter: String,
        forceFetch: Boolean
    ): MutableLiveData<List<DrinksDataModel>> {
        mCallback = callback
        if (!forceFetch && mDrinksList.value?.isNotEmpty() == true) {
            mCallback?.onNetworkSuccess()
        } else {
            val call = apiServices.searchByFirstLetter(firstLetter)
            enqueueDrinksCall(call)
        }
        return mDrinksList
    }

    fun getDrinksByName(
        callback: NetworkResponseCallback,
        query: String,
        forceFetch: Boolean
    ): MutableLiveData<List<DrinksDataModel>> {
        mCallback = callback
        if (!forceFetch && mDrinksList.value?.isNotEmpty() == true) {
            mCallback?.onNetworkSuccess()
        } else {
            val call = apiServices.searchDrinksByName(query)
            enqueueDrinksCall(call)
        }
        return mDrinksList
    }

    private fun enqueueDrinksCall(call: Call<DrinksContainer>) {
        call.enqueue(object : Callback<DrinksContainer> {
            override fun onResponse(
                call: Call<DrinksContainer>,
                response: Response<DrinksContainer>
            ) {
                if (response.isSuccessful) {
                    mDrinksList.value = response.body()?.drinks
                    mCallback?.onNetworkSuccess()
                } else {
                    mDrinksList.value = emptyList()
                    mCallback?.onNetworkFailure(Throwable("Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<DrinksContainer>, t: Throwable) {
                mDrinksList.value = emptyList()
                mCallback?.onNetworkFailure(t)
            }
        })
    }
}







