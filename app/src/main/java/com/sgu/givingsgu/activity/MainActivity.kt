package com.sgu.givingsgu.activity

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.R
import com.sgu.givingsgu.activity.Fragment.RedeemFragment
import com.sgu.givingsgu.activity.Fragment.HomeFragment
import com.sgu.givingsgu.activity.Fragment.ProfileFragment
import com.sgu.givingsgu.activity.Fragment.NotificationFragment
import com.sgu.givingsgu.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var notificationFragment: NotificationFragment
    private lateinit var redeemFragment: RedeemFragment
    private lateinit var profileFragment: ProfileFragment
    private var activeFragment: Fragment? = null

    private var curPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        homeFragment = HomeFragment()
        notificationFragment = NotificationFragment()
        redeemFragment = RedeemFragment()
        profileFragment = ProfileFragment()
        activeFragment = homeFragment

        supportFragmentManager.beginTransaction().apply {
            add(binding.contentFrame.id, homeFragment, "HomeFragment").show(homeFragment)
            add(binding.contentFrame.id, redeemFragment, "RedeemFragment").hide(redeemFragment)
            add(binding.contentFrame.id, notificationFragment, "SearchFragment").hide(notificationFragment)
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
                    switchFragment(redeemFragment, curPos, 1)
                    curPos = 1

                }

                2 -> {
                    switchFragment(notificationFragment, curPos, 2)
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

//    private fun switchFragment(fragment: Fragment, curPos: Int, newPos: Int) {
//        if (fragment != activeFragment) {
//            lateinit var curAnim: Animation
//            lateinit var newAnim: Animation
//            if (newPos > curPos) {
//                curAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_in_left)
//                newAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_out_left)
//            } else {
//                curAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_in_right)
//                newAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_out_right)
//            }
//            // Set animation for fragment entering and exiting
//            fragment.view?.startAnimation(curAnim)
//            activeFragment?.view?.startAnimation(newAnim)
//            supportFragmentManager.beginTransaction().apply {
//
//                hide(activeFragment!!)
//                show(fragment)
//            }.commit()
//            activeFragment = fragment
//        }
//    }
private fun switchFragment(fragment: Fragment, curPos: Int, newPos: Int) {
    if (fragment != activeFragment) {
        val transaction = supportFragmentManager.beginTransaction()

        // Thêm hiệu ứng trượt cho chuyển đổi fragment
        if (newPos > curPos) {
            transaction.setCustomAnimations(
                R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left
            )
        } else {
            transaction.setCustomAnimations(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right
            )
        }

        // Ẩn Fragment đang hiện tại và hiện Fragment mới
        activeFragment?.let { transaction.hide(it) }
        transaction.show(fragment).commitNowAllowingStateLoss()

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