package com.example.sofittask

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sofittask.databinding.ActivityMainBinding
import com.example.sofittask.databinding.CustomTabLayoutBinding
import com.example.sofittask.presentation.ui.adapters.CustomPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize() {
        val adapter = CustomPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.customView = adapter.getTabView(position, binding.tabLayout)
        }.attach()


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateTabColors(tab.customView, tab.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                updateTabColors(tab.customView, tab.position, false)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Optional: Handle reselection of a tab
            }
        })

        // By default, select the first tab
        binding.tabLayout.getTabAt(0)?.select()


    }

    // Function to update tab colors
    private fun updateTabColors(view: View?, position: Int, isSelected: Boolean) {
        if (view == null) return

        val binding = CustomTabLayoutBinding.bind(view)

        // Set selected tab background color, text color, and icon tint color
        if (isSelected) {
            binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.app_primary_color
                )
            )
            binding.tabText.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.white
                )
            )
            binding.tabIcon.imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.white))
        } else {
            // Set unselected tab background color, text color, and icon tint color
            binding.root.setBackgroundColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.white
                )
            )
            binding.tabText.setTextColor(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.app_primary_color
                )
            )
            binding.tabIcon.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    binding.root.context,
                    R.color.app_primary_color
                )
            )
        }


    }
}
