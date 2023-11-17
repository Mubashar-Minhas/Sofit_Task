package com.example.sofittask.presentation.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sofittask.R
import com.example.sofittask.presentation.ui.adapters.DrinksAdapter
import com.example.sofittask.databinding.FragmentHomeBinding
import com.example.sofittask.presentation.ui.interfaces.OnItemClickListener
import com.example.sofittask.presentation.ui.model.DrinksDataModel
import com.example.sofittask.utils.SharedPref
import com.example.sofittask.viewmodels.DrinksByNameViewModel
import com.example.sofittask.viewmodels.FavoritesViewModel




class HomeFragment : Fragment(), OnItemClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mAdapter: DrinksAdapter
    private lateinit var mViewModel: DrinksByNameViewModel
    private lateinit var favoritesViewModel: FavoritesViewModel
    private var searchedText: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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
        mViewModel = ViewModelProvider(this)[DrinksByNameViewModel::class.java]
        favoritesViewModel = ViewModelProvider(requireActivity())[FavoritesViewModel::class.java]
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
        mViewModel.fetchDrinksByFirstLetter(searchedText!!, false)
            .observe(viewLifecycleOwner, Observer { kt ->
                Log.d("list", kt.toString())
                mAdapter.setData(kt)
                mAdapter.notifyDataSetChanged()
            })
        mViewModel.mShowApiError.observe(viewLifecycleOwner, Observer {
            // AlertDialog.Builder(this).setMessage(it).show()
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        })
        mViewModel.mShowProgressBar.observe(viewLifecycleOwner, Observer { bt ->
            if (bt) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        mViewModel.mShowNetworkError.observe(viewLifecycleOwner, Observer {
            //AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()

        })
    }

    private fun drinksByName(searchedText: String) {
        mViewModel.fetchDrinksByName(searchedText!!, false)
            .observe(viewLifecycleOwner, Observer { kt ->
                mAdapter.setData(kt)
            })
        mViewModel.mShowApiError.observe(viewLifecycleOwner, Observer {
            // AlertDialog.Builder(this).setMessage(it).show()
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        })
        mViewModel.mShowProgressBar.observe(viewLifecycleOwner, Observer { bt ->
            if (bt) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
        mViewModel.mShowNetworkError.observe(viewLifecycleOwner, Observer {
            //AlertDialog.Builder(this).setMessage(R.string.app_no_internet_msg).show()
            Toast.makeText(requireContext(), "No Internet", Toast.LENGTH_SHORT).show()

        })
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onItemClick(position: Int, clickedItem: DrinksDataModel) {
        SharedPref.saveFavorite(requireContext(), clickedItem)

     if (SharedPref.getFavorites(requireContext()).isNotEmpty()){
         Log.d("list", SharedPref.getFavorites(requireContext()).toString())
         favoritesViewModel.updateFavorites(SharedPref.getFavorites(requireContext()))

         }

}}