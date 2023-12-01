package com.example.sofittask.presentation.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofittask.R
import com.example.sofittask.databinding.FragmentHomeBinding
import com.example.sofittask.presentation.ui.adapters.DrinksAdapter
import com.example.sofittask.presentation.ui.interfaces.OnItemClickListener
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import com.example.sofittask.utils.SharedPref
import com.example.sofittask.viewmodels.DrinksByNameViewModel
import com.example.sofittask.viewmodels.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdapter: DrinksAdapter
    private val mViewModel: DrinksByNameViewModel by viewModels()
    private val favoritesViewModel: FavoritesViewModel by activityViewModels()
    private var searchedText: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //mViewModel = ViewModelProvider(this)[DrinksByNameViewModel::class.java]
        //favoritesViewModel = ViewModelProvider(requireActivity())[FavoritesViewModel::class.java]
        init()
        initializeRecyclerView()
        initializeObservers()
    }

    private fun init() {
        if (!SharedPref.getCheckedState(requireContext()).isNullOrEmpty()) {
            if (SharedPref.getCheckedState(requireContext())
                    .toString() == SharedPref.BY_NAME
            ) {
                binding.radioButtonStart.isChecked = true
            } else {
                binding.radioButtonEnd.isChecked = true
            }
        } else {
            binding.radioButtonStart.isChecked = true
        }

        binding.radioButton.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonStart -> {
                    SharedPref.saveCheckedState(requireContext(), SharedPref.BY_NAME)
                    mAdapter.resetData()
                    if (!searchedText.isNullOrEmpty()) {
                        drinksByName("margarita")
                    }
                }

                R.id.radioButtonEnd -> {
                    SharedPref.saveCheckedState(requireContext(), SharedPref.BY_ALPHABET)
                    mAdapter.resetData()

                    if (!searchedText.isNullOrEmpty()) {
                        drinksByFirstAlphabet("a")
                    }
                }
            }
        }

        binding.mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchedText = query.toString()
                if (binding.radioButtonStart.isChecked) {
                    drinksByName(searchedText!!)
                } else {
                    drinksByFirstAlphabet(searchedText!!)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchedText = newText
                if (binding.radioButtonStart.isChecked) {
                    drinksByName(searchedText!!)
                } else {
                    drinksByFirstAlphabet(searchedText!!)
                }
                return true
            }
        })

    }


    private fun initializeRecyclerView() {
        mAdapter = DrinksAdapter(this)
        binding.drinksRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }


    private fun initializeObservers() {
        if (searchedText == null) {
            searchedText = "margarita"
        }
        if (binding.radioButtonStart.isChecked) {
            drinksByName(searchedText!!)
        } else {
            drinksByFirstAlphabet(searchedText!!)
        }

    }


    private fun drinksByFirstAlphabet(searchedText: String) {
        mViewModel.fetchDrinksByFirstLetter(searchedText, false).observe(viewLifecycleOwner) { drinksList ->
            mAdapter.setData(drinksList)
            mAdapter.notifyDataSetChanged()
            Log.d("list", drinksList.toString())
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        mViewModel.mShowApiError.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        mViewModel.mShowProgressBar.observe(viewLifecycleOwner) { showProgressBar ->
            binding.progressBar.visibility = if (showProgressBar) View.VISIBLE else View.GONE
        }

        mViewModel.mShowNetworkError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()
        }
    }


    private fun drinksByName(searchedText: String) {
        mViewModel.fetchDrinksByName(searchedText, false).observe(viewLifecycleOwner) { drinksList ->
            mAdapter.setData(drinksList)
        }

        // Observing common LiveData moved to a shared method
        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onItemClick(position: Int, clickedItem: DrinksDataModel) {
        // Save the clicked item as a favorite
        SharedPref.saveFavorite(requireContext(), clickedItem)

        // Retrieve the updated list of favorites
        val favorites = SharedPref.getFavorites(requireContext())

        // Check if the favorites list is not empty
        if (favorites.isNotEmpty()) {
            Log.d("list", favorites.toString())
            // Update the favorites in the ViewModel
            favoritesViewModel.updateFavorites(favorites)
        }
    }
}