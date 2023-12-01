package com.example.sofittask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel() {
    private val _favorites = MutableLiveData<List<DrinksDataModel>>()
    val favorites: LiveData<List<DrinksDataModel>> get() = _favorites

    fun updateFavorites(newFavorites: List<DrinksDataModel>) {
        _favorites.value = newFavorites
    }

}



