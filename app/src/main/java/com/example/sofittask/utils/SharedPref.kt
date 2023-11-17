package com.example.sofittask.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SharedPref {
    private const val PREF_NAME = "YourSharedPreferencesName"
    private const val FAVORITE_ITEMS_KEY = "favoriteItems"
    const val BY_NAME: String = "byName"
     const val BY_ALPHABET: String = "byAlphabet"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveFavorite(context: Context, drink: DrinksDataModel) {
        val favoritesList = getFavorites(context).toMutableList()

        if (favoritesList.contains(drink)) {
            // Remove the item if it's already in favorites
            favoritesList.remove(drink)
        } else {
            // Add the item if it's not in favorites
            favoritesList.add(drink)
        }

        // Save the updated favorites list
        saveFavorites(context, favoritesList)
    }


    fun saveFavorites(context: Context, favoritesList: List<DrinksDataModel>) {
        val editor = getSharedPreferences(context).edit()
        val json = Gson().toJson(favoritesList)
        editor.putString(FAVORITE_ITEMS_KEY, json)
        editor.apply()
    }

    fun getFavorites(context: Context): List<DrinksDataModel> {
        val json = getSharedPreferences(context).getString(FAVORITE_ITEMS_KEY, "")
        val type = object : TypeToken<List<DrinksDataModel>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }

    fun saveCheckedState(context: Context, checkedButton: String) {
        getSharedPreferences(context).edit().putString("checkedRadioButton", checkedButton).apply()
    }

    fun getCheckedState(context: Context): String? {
        return getSharedPreferences(context).getString("checkedRadioButton", "")
    }
}
