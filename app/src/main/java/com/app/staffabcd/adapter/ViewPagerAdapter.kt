package com.app.staffabcd.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.staffabcd.fragments.reportFragments.*

class ViewPagerAdapter(
    fm: FragmentManager
) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0)
            fragment = LevelOneFragment()
        else if (position == 1)
            fragment = LevelTwoFragment()
        else if (position == 2)
            fragment = LevelThreeFragment()
        else if (position == 3)
            fragment = HistoryFragment()
        else if (position == 4)
            fragment = LevelFiveFragment()

        return fragment!!
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0)
            title = "Level 1"
        else if (position == 1)
            title ="Level 2"
        else if (position == 2) title = "Level 3"
        else if (position == 3) title = "Level 4"
        else if (position == 4) title = "Level 5"

        return title
    }
}