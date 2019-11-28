package com.example.budayaku

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.budayaku.adapters.ViewPagerFragmentAdapter
import com.example.budayaku.fragments.AccountFragment
import com.example.budayaku.fragments.ForumFragment
import com.example.budayaku.fragments.HomeFragment
import com.example.budayaku.fragments.QuestFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = ViewPagerFragmentAdapter(supportFragmentManager)

        fragmentAdapter.addFragment(HomeFragment())
        fragmentAdapter.addFragment(QuestFragment())
        fragmentAdapter.addFragment(ForumFragment())
        fragmentAdapter.addFragment(AccountFragment())

        vp_container.apply {
            adapter = fragmentAdapter
            offscreenPageLimit = 5
            currentItem = 0
        }

        nav_bottom.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    vp_container.setCurrentItem(0, true)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.quest -> {
                    vp_container.setCurrentItem(1, true)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.forum -> {
                    vp_container.setCurrentItem(2, true)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.account -> {
                    vp_container.setCurrentItem(3, true)
                    return@setOnNavigationItemSelectedListener true
                }
            }

            false
        }

        vp_container.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (prevMenuItem != null)
                    prevMenuItem!!.isChecked = false
                else
                    nav_bottom.menu.getItem(0).isChecked = false

                nav_bottom.menu.getItem(position).isChecked = true
                prevMenuItem = nav_bottom.menu.getItem(position)
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })
    }

//    override fun onBackPressed() {
//        finish()
//    }
}
