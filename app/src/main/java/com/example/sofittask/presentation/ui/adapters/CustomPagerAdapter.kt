package com.example.sofittask.presentation.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sofittask.presentation.ui.HomeFragment
import com.example.sofittask.R
import com.example.sofittask.databinding.CustomTabLayoutBinding
import com.example.sofittask.presentation.ui.FavoriteFragment
import com.google.android.material.tabs.TabLayout

class CustomPagerAdapter(private val activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val tabTitles = listOf("Home", "Favorite")
    private val tabIcons = listOf(
        R.drawable.ic_nav_home, // Your tab icons
        R.drawable.ic_nav_favorite,

        )

    override fun getItemCount(): Int = tabTitles.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> FavoriteFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    fun getTabView(position: Int, tabLayout: TabLayout): View {
        val binding =
            CustomTabLayoutBinding.inflate(LayoutInflater.from(tabLayout.context), tabLayout, false)

        binding.tabIcon.setImageResource(tabIcons[position])
        binding.tabText.text = tabTitles[position]
        if (position == 0) {
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

        }
        return binding.root
    }


}

