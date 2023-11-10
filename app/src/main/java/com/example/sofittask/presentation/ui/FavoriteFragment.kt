package com.example.sofittask.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofittask.R
import com.example.sofittask.presentation.ui.adapters.FavoriteAdapter
import com.example.sofittask.databinding.FragmentFavorite2Binding
import com.example.sofittask.utils.SharedPref

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavorite2Binding
    private lateinit var mAdapter: FavoriteAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavorite2Binding.inflate(inflater, container, false)
        init()
        return binding.root
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
    }


}