package com.example.sofittask.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sofittask.R
import com.example.sofittask.databinding.ItemRecyclerviewBinding
import com.example.sofittask.presentation.ui.interfaces.OnItemClickListener
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import com.example.sofittask.utils.SharedPref

class DrinksAdapter(private val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<DrinksAdapter.DrinkViewHolder>() {
    private var drinksList: List<DrinksDataModel>? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val binding =
            ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DrinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val drink = drinksList?.get(position)
        if (drink != null) {
            holder.bind(drink)
        }
    }

    override fun getItemCount(): Int {
        return drinksList?.size!!
    }

    fun setData(drinks: List<DrinksDataModel>?) {
        drinksList = drinks
        notifyDataSetChanged()

    }

    fun resetData() {
        drinksList = emptyList()
        notifyDataSetChanged()
    }

    inner class DrinkViewHolder(private val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        fun bind(drink: DrinksDataModel) {
            // Bind data to the ViewHolder views
            binding.apply {
                drinkName.text = drink.strDrink
                drinkDescription.text = drink.strInstructions
                Glide.with(context)
                    .load(drink.strDrinkThumb)
                    .into(drinkImage)

                // Check if the current item is in the saved favorites list
                val isFavorite = SharedPref.getFavorites(context).contains(drink)

                // Set the star icon based on the favorite state
                mFavImage.setImageResource(if (isFavorite) R.drawable.star_yellow else R.drawable.ic_nav_favorite)

                // Set check state
                mCheckBox.isChecked = drink.strAlcoholic == "Alcoholic"
            }
        }

        init {
            binding.mFavImage.setOnClickListener {
                // Toggle the favorite state
                val drink = drinksList?.get(adapterPosition)
                val favorites = SharedPref.getFavorites(context).toMutableList()
                val isFavorite = favorites.contains(drink)

                if (isFavorite) {
                    // Remove the item from favorites if it's already selected
                    favorites.remove(drink)
                    binding.mFavImage.setImageResource(R.drawable.ic_nav_favorite)
                } else {
                    // Add the item to favorites if it's not selected
                    drink?.let { favorites.add(it) }
                    binding.mFavImage.setImageResource(R.drawable.star_yellow)
                }

                // Save the updated favorites list to SharedPreferences
               // SharedPref.saveFavorites(context, favorites)

                // Notify the item click listener
                if (drink != null) {
                    itemClickListener.onItemClick(adapterPosition, drink)
                }
            }
        }

    }

}