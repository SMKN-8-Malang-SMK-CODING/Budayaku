package com.example.budayaku.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerFragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val listFragment = ArrayList<Fragment>()

    fun addFragment(fragment: Fragment) {
        listFragment.add(fragment)
    }

    override fun getItem(position: Int): Fragment = listFragment[position]

    override fun getCount(): Int = listFragment.size
}