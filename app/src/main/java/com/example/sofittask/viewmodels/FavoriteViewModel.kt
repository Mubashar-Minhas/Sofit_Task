package com.example.sofittask.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sofittask.presentation.ui.model.DrinksDataModel

class FavoritesViewModel : ViewModel() {
    private val _favorites = MutableLiveData<List<DrinksDataModel>>()
    val favorites: LiveData<List<DrinksDataModel>> get() = _favorites

    fun updateFavorites(newFavorites: List<DrinksDataModel>) {
        _favorites.value = newFavorites
    }
}
