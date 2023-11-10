package com.example.sofittask.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.sofittask.presentation.ui.interfaces.NetworkResponseCallback
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import com.example.sofittask.repositories.DrinksRepository
import com.example.sofittask.utils.NetworkHelper

class DrinksByNameViewModel(private val app: Application) : AndroidViewModel(app) {

    // LiveData for the list of drinks
    private var mList: MutableLiveData<List<DrinksDataModel>> =
        MutableLiveData<List<DrinksDataModel>>().apply { value = emptyList() }
    private var mList1: MutableLiveData<List<DrinksDataModel>> =
        MutableLiveData<List<DrinksDataModel>>().apply { value = emptyList() }

    // LiveData to show/hide progress bar
    val mShowProgressBar = MutableLiveData(true)

    // LiveData to show/hide network error
    val mShowNetworkError: MutableLiveData<Boolean> = MutableLiveData()

    // LiveData to show API error message
    val mShowApiError = MutableLiveData<String>()

    // Repository to handle data retrieval
    private var mRepository = DrinksRepository.getInstance()



    // Function to fetch drinks by name from the server
    fun fetchDrinksByName(query:String,forceFetch: Boolean): MutableLiveData<List<DrinksDataModel>> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true
            mList = mRepository.getDrinksByName(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

                override fun onNetworkSuccess() {
                    mShowProgressBar.value = false
                }
            },query, forceFetch)
        } else {
            mShowNetworkError.value = true
        }

        return mList
    }

    // Function to fetch drinks by first letter from the server
    fun fetchDrinksByFirstLetter(firstLetter: String, forceFetch: Boolean): MutableLiveData<List<DrinksDataModel>> {
        if (NetworkHelper.isOnline(app.baseContext)) {
            mShowProgressBar.value = true

            mList1 = mRepository.getDrinksByFirstLetter(object : NetworkResponseCallback {
                override fun onNetworkFailure(th: Throwable) {
                    mShowApiError.value = th.message
                }

                override fun onNetworkSuccess() {
                    mShowProgressBar.value = false
                }
            }, firstLetter, forceFetch)
        } else {
            mShowNetworkError.value = true
        }

        return mList1
    }


}


