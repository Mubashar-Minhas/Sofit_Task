package com.example.sofittask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sofittask.presentation.ui.interfaces.NetworkResponseCallback
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import com.example.sofittask.repositories.DrinksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DrinksByNameViewModel @Inject constructor(
    private val mRepository: DrinksRepository
) : ViewModel() {

    private var mList = MutableLiveData<List<DrinksDataModel>>().apply { value = emptyList() }
    private var mList1 = MutableLiveData<List<DrinksDataModel>>().apply { value = emptyList() }

    val mShowProgressBar = MutableLiveData<Boolean>()
    val mShowNetworkError = MutableLiveData<Boolean>()
    val mShowApiError = MutableLiveData<String>()

    fun fetchDrinksByName(query: String, forceFetch: Boolean): MutableLiveData<List<DrinksDataModel>> {
        mShowProgressBar.value = true
        mList = mRepository.getDrinksByName(object : NetworkResponseCallback {
            override fun onNetworkFailure(th: Throwable) {
                mShowApiError.value = th.message
                mShowProgressBar.value = false
            }

            override fun onNetworkSuccess() {
                mShowProgressBar.value = false
            }
        }, query, forceFetch)

        return mList
    }

    fun fetchDrinksByFirstLetter(firstLetter: String, forceFetch: Boolean): MutableLiveData<List<DrinksDataModel>> {
        mShowProgressBar.value = true
        mList1 = mRepository.getDrinksByFirstLetter(object : NetworkResponseCallback {
            override fun onNetworkFailure(th: Throwable) {
                mShowApiError.value = th.message
                mShowProgressBar.value = false
            }

            override fun onNetworkSuccess() {
                mShowProgressBar.value = false
            }
        }, firstLetter, forceFetch)

        return mList1
    }
}



