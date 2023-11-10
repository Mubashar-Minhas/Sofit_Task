package com.example.sofittask.presentation.ui.interfaces

import com.example.sofittask.presentation.ui.model.DrinksDataModel
import java.text.FieldPosition

interface OnItemClickListener {
    fun onItemClick(position: Int,clickedItem: DrinksDataModel)
}