package com.github.gituser.ui.main.detail

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.gituser.ui.main.detail.FollowFragment
import com.github.gituser.ui.main.detail.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity,
                           private val username: String
) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowFragment.newInstance(username)
            1 -> fragment = FollowingFragment.newInstance(username)
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}