package com.astrika.stywis_staff.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.astrika.stywis_staff.view.dashboard.fragments.DashboardSubFragment

class DashboardPagerAdapter(activity: FragmentActivity, private val itemsCount: Array<String>) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount.size
    }

    override fun createFragment(position: Int): Fragment {
        return DashboardSubFragment.getInstance(position)
    }
}