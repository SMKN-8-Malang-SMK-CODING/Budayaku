package com.example.budayaku

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.budayaku.adapters.ViewPagerFragmentAdapter
import com.example.budayaku.databases.User
import com.example.budayaku.fragments.AccountFragment
import com.example.budayaku.fragments.ForumFragment
import com.example.budayaku.fragments.HomeFragment
import com.example.budayaku.fragments.QuestFragment
import com.example.budayaku.utils.AuthHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_user_account.*
import kotlinx.android.synthetic.main.fragment_account.*

class MainActivity : AppCompatActivity() {

    private var prevMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = ViewPagerFragmentAdapter(supportFragmentManager)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("users").document(currentUser.uid).get()
                .addOnSuccessListener {
                    val data: User? = it.toObject(User::class.java)
                    user_usname.text = currentUser.email
                }
        }

        fragmentAdapter.addFragment(HomeFragment())
        fragmentAdapter.addFragment(QuestFragment())
        fragmentAdapter.addFragment(ForumFragment())
        fragmentAdapter.addFragment(AccountFragment())

        tv_userLogout.setOnClickListener {
            AuthHelper.signOut(this)
        }

        vp_container.apply {
            adapter = fragmentAdapter
            offscreenPageLimit = 4
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

    override fun onBackPressed() {
        finish()
    }
}
