package com.example.sofittask.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofittask.R
import com.example.sofittask.presentation.ui.adapters.FavoriteAdapter
import com.example.sofittask.databinding.FragmentFavorite2Binding
import com.example.sofittask.utils.SharedPref
import com.example.sofittask.viewmodels.FavoritesViewModel

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavorite2Binding
    private lateinit var mAdapter: FavoriteAdapter
    private lateinit var favoritesViewModel: FavoritesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavorite2Binding.inflate(inflater, container, false)
        favoritesViewModel = ViewModelProvider(requireActivity())[FavoritesViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.mFragmentTitle.fragmentTitleTextView.text = getString(R.string.favorite_recipes)
        mAdapter = FavoriteAdapter()
        binding.favoriteRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }

        val favoriteList = SharedPref.getFavorites(requireContext())
        if (favoriteList.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Favorite list is empty", Toast.LENGTH_SHORT).show()
        } else {
            mAdapter.setFavorites(favoriteList)
        }


        favoritesViewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            if (favorites != null) {
                // Check if favorites is not null before updating the adapter
                mAdapter.updateFavorite(favorites)
                mAdapter.notifyDataSetChanged()
            } else {
                // Log an error or show a toast to indicate that favorites is null
                Log.e("FavoriteFragment", "Favorites list is null.")
                Toast.makeText(requireContext(), "Favorites list is null", Toast.LENGTH_SHORT).show()
            }
        }

        Log.d("list", null.toString())



    }


}