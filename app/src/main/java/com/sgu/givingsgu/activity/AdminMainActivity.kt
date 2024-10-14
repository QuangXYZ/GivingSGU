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
import com.sgu.givingsgu.R
import com.sgu.givingsgu.activity.Fragment.AdminHomeFragment
import com.sgu.givingsgu.activity.Fragment.AdminProfileFragment
import com.sgu.givingsgu.activity.Fragment.CreateFragment
import com.sgu.givingsgu.databinding.AdminActivityMainBinding

class AdminMainActivity : AppCompatActivity() {
    private lateinit var binding: AdminActivityMainBinding
    private lateinit var adminHomeFragment: AdminHomeFragment
    private lateinit var createFragment: CreateFragment
    private lateinit var adminProfileFragment: AdminProfileFragment
    private var activeFragment: Fragment? = null

    private var curPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdminActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adminHomeFragment = AdminHomeFragment()
        createFragment = CreateFragment()
        adminProfileFragment = AdminProfileFragment()
        activeFragment = adminHomeFragment

        supportFragmentManager.beginTransaction().apply {
            add(binding.adminContentFrame.id, adminHomeFragment, "AdminHomeFragment").show(
                adminHomeFragment
            )
            add(binding.adminContentFrame.id, createFragment, "CreateFragment").show(createFragment)
            add(binding.adminContentFrame.id, adminProfileFragment, "AdminProfileFragment").show(
                adminProfileFragment
            )
        }.commit()

        settingUpListener()

    }

    private fun settingUpListener() {
        binding.adminBottomBar.setOnItemSelectedListener { item ->
            when (item) {
                0 -> {
                    switchFragment(adminHomeFragment, curPos, 0)
                    curPos = 0
                }

                1 -> {
                    switchFragment(createFragment, curPos, 0)
                    curPos = 1
                }

                2 -> {
                    switchFragment(adminProfileFragment, curPos, 2)
                    curPos = 2
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
                curAnim =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_in_left)
                newAnim =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_out_left)
            } else {
                curAnim =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_in_right)
                newAnim =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.anim_slide_out_right)
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