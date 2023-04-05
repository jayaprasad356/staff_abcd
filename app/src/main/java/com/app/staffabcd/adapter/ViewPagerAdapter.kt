package com.app.staffabcd.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.staffabcd.fragments.reportFragments.HistoryFragment
import com.app.staffabcd.fragments.reportFragments.LevelOneFragment
import com.app.staffabcd.fragments.reportFragments.LevelThreeFragment
import com.app.staffabcd.fragments.reportFragments.LevelTwoFragment

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
        return fragment!!
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0)
            title = "Level 1"
        else if (position == 1)
            title ="Level 2"
        else if (position == 2) title = "Level 3"
        else if (position == 3) title = "History"

        return title
    }
}