package com.quetta.touristan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.quetta.touristan.R
import com.quetta.touristan.log
import com.quetta.touristan.ui.home.HomeFragment
import com.quetta.touristan.ui.main.SectionsPagerAdapter

class PagerFragment : Fragment() {

    private var category: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            category = args.getInt("category")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_pager, container, false)
        log("args ${arguments}")

        if(category == null)
            category = R.string.category_restaurant
        category?.let { cateId ->
            val k = getString(R.string.arg_category)
            val categoryStr = getString(cateId)

            val homeFrag = HomeFragment()
            putArg(homeFrag, k, categoryStr)
//            when(cateId) {
//                R.string.category_hospital -> putArg(homeFrag, k, categoryStr)
//                R.string.category_bank -> HomeFragment()
//                R.string.category_restaurant -> HomeFragment()
//                R.string.category_university -> HomeFragment()
//                R.string.category_picnic -> HomeFragment()
//            }

            val fragments = listOf(
                homeFrag,
                DetailsFragment()
            )

            val activity = requireActivity() as MainActivity

            val sectionsPagerAdapter = SectionsPagerAdapter(requireContext(), fragments, childFragmentManager)
            val viewPager: ViewPager = view.findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter

            val tabs: TabLayout = activity.findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)
        }


        return view
    }

    private fun putArg(fragment: HomeFragment, key: String, value: String) {
        fragment.apply {
            arguments = Bundle().apply {
                putString(key, value)
            }
        }
    }

}