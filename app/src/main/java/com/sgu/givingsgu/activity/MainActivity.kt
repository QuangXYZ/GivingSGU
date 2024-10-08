package com.sgu.givingsgu.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.R
import com.sgu.givingsgu.activity.Fragment.HistoryFragment
import com.sgu.givingsgu.activity.Fragment.HomeFragment
import com.sgu.givingsgu.activity.Fragment.ProfileFragment
import com.sgu.givingsgu.activity.Fragment.SearchFragment
import com.sgu.givingsgu.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var searchFragment: SearchFragment
    private lateinit var historyFragment: HistoryFragment
    private lateinit var profileFragment: ProfileFragment
    private var activeFragment: Fragment? = null

    private var curPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        homeFragment = HomeFragment()
        searchFragment = SearchFragment()
        historyFragment = HistoryFragment()
        profileFragment = ProfileFragment()
        activeFragment = homeFragment

        supportFragmentManager.beginTransaction().apply {
            add(binding.contentFrame.id, homeFragment, "HomeFragment").show(homeFragment)
            add(binding.contentFrame.id, searchFragment, "SearchFragment").hide(searchFragment)
            add(binding.contentFrame.id, historyFragment, "HistoryFragment").hide(historyFragment)
            add(binding.contentFrame.id, profileFragment, "ProfileFragment").hide(profileFragment)
        }.commit()

        settingUpListener()
    }


    fun settingUpListener() {
        binding.bottomBar.setOnItemSelectedListener { item ->

            when (item) {
                0 -> {
                    switchFragment(homeFragment, curPos, 0)
                    curPos = 0

                }

                1 -> {
                    switchFragment(searchFragment, curPos, 1)
                    curPos = 1

                }

                2 -> {
                    switchFragment(historyFragment, curPos, 2)
                    curPos = 2

                }

                3 -> {
                    switchFragment(profileFragment, curPos, 3)
                    curPos = 3

                }

                else -> false
            }

        }

    }

    private fun switchFragment(fragment: Fragment, curPos: Int, newPos: Int) {
        if (fragment != activeFragment) {
            lateinit var curAnim: Animation
            lateinit var newAnim: Animation
            if (newPos > curPos) {
                curAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_in_left)
                newAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_out_left)
            } else {
                curAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_in_right)
                newAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_out_right)
            }
            // Set animation for fragment entering and exiting
            fragment.view?.startAnimation(curAnim)
            activeFragment?.view?.startAnimation(newAnim)
            supportFragmentManager.beginTransaction().apply {

                hide(activeFragment!!)
                show(fragment)
            }.commit()
            activeFragment = fragment
        }
    }
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}