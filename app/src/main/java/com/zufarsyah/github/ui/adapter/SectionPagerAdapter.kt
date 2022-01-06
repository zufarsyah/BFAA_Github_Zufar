package com.zufarsyah.github.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zufarsyah.github.ui.fragments.FollowFragment

class SectionPagerAdapter(activity: AppCompatActivity, data: String) : FragmentStateAdapter(activity) {

    private var dataUsername: String = data

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position + 1, dataUsername)
    }

    override fun getItemCount(): Int = 2
}