package com.example.sofittask.presentation.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sofittask.R
import com.example.sofittask.databinding.ItemRecyclerviewBinding
import com.example.sofittask.presentation.ui.model.DrinksDataModel

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private var favoriteList: List<DrinksDataModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteItem = favoriteList?.get(position)
        if (favoriteItem != null) {
            holder.bind(favoriteItem)
        }
    }

    override fun getItemCount(): Int {
        return favoriteList?.size ?: 0
    }

    fun setFavorites(favorites: List<DrinksDataModel>?) {
        if (favorites != null) {
            favoriteList = favorites
        }
        notifyDataSetChanged()
    }

    fun updateFavorite(favorites: List<DrinksDataModel>) {
        favoriteList = favorites
        notifyDataSetChanged()
    }

    inner class FavoriteViewHolder(private val binding: ItemRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteItem: DrinksDataModel) {
            // Bind data to the ViewHolder views
            val context = binding.root.context
            binding.drinkName.text = favoriteItem.strDrink
            binding.drinkDescription.text = favoriteItem.strInstructions
            Glide.with(context)
                .load(favoriteItem.strDrinkThumb)
                .into(binding.drinkImage)

            binding.mFavImage.setImageResource(R.drawable.star_yellow)

            //set check state
            binding.mCheckBox.isChecked = favoriteItem.strAlcoholic == "Alcoholic"
        }
    }
}
