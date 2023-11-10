package com.example.sofittask.presentation.ui.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DrinksContainer(

    @SerializedName("drinks" ) var drinks : ArrayList<DrinksDataModel> = arrayListOf()
):Parcelable
