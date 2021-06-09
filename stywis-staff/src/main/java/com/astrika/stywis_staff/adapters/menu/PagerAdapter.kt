package com.astrika.stywis_staff.adapters.menu

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class PagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm!!) {

    private var fragmentList = ArrayList<Fragment>()
    private var fragmentTitleList = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
//        return MenuPagerFragment.newInstance("","")
        return fragmentList[position]

    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitleList.get(position)
    }
}
