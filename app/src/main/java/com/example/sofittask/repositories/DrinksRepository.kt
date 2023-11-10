package com.example.sofittask.repositories

import androidx.lifecycle.MutableLiveData
import com.example.sofittask.presentation.ui.interfaces.NetworkResponseCallback
import com.example.sofittask.presentation.ui.model.DrinksContainer
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import com.example.sofittask.network.RestClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DrinksRepository private constructor() {
    private lateinit var mCallback: NetworkResponseCallback
    private var mDrinksList: MutableLiveData<List<DrinksDataModel>> =
        MutableLiveData<List<DrinksDataModel>>().apply { value = emptyList() }
    private var mDrinksList1: MutableLiveData<List<DrinksDataModel>> =
        MutableLiveData<List<DrinksDataModel>>().apply { value = emptyList() }

    companion object {
        private var mInstance: DrinksRepository? = null
        fun getInstance(): DrinksRepository {
            if (mInstance == null) {
                synchronized(this) {
                    mInstance = DrinksRepository()
                }
            }
            return mInstance!!
        }
    }

    private lateinit var mDrinksCall: Call<DrinksContainer>



    fun getDrinksByFirstLetter(
        callback: NetworkResponseCallback,
        firstLetter: String,
        forceFetch: Boolean
    ): MutableLiveData<List<DrinksDataModel>> {
        mCallback = callback
        mDrinksList.value= emptyList()
        if (mDrinksList.value!!.isNotEmpty() && !forceFetch) {
            mCallback.onNetworkSuccess()
            return mDrinksList
        }

        mDrinksCall = RestClient.getInstance().getApiService().searchByFirstLetter(firstLetter)
        enqueueDrinksCall(mDrinksCall)

        return mDrinksList
    }

    fun getDrinksByName(
        callback: NetworkResponseCallback,
        query: String,
        forceFetch: Boolean
    ): MutableLiveData<List<DrinksDataModel>> {
        mDrinksList.value= emptyList()
        mCallback = callback
        if (mDrinksList.value!!.isNotEmpty() && !forceFetch) {
            mCallback.onNetworkSuccess()
            return mDrinksList
        }

        mDrinksCall = RestClient.getInstance().getApiService().searchDrinksByName(query)
        enqueueDrinksCall(mDrinksCall)
        return mDrinksList
    }

    private fun enqueueDrinksCall(mDrinksCall: Call<DrinksContainer>) {
        mDrinksCall.enqueue(object : Callback<DrinksContainer> {
            override fun onResponse(
                call: Call<DrinksContainer>,
                response: Response<DrinksContainer>
            ) {
                mDrinksList.value = response.body()?.drinks
                mCallback.onNetworkSuccess()
            }

            override fun onFailure(call: Call<DrinksContainer>, t: Throwable) {
                mDrinksList.value = emptyList()
                mCallback.onNetworkFailure(t)
            }
        })
    }
}






