package com.quetta.touristan.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.quetta.touristan.R

private val TAB_TITLES = arrayOf(
    R.string.tab_list,
    R.string.tab_locations
)


class SectionsPagerAdapter(private val context: Context, private val fragments: List<Fragment>, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return fragments.size
    }
}