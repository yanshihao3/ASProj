package com.zq.asproj

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.umeng.message.PushAgent
import com.zq.asproj.logic.MainActivityLogic
import com.zq.common.ui.component.BaseActivity
import com.zq.hilibrary.util.DataBus


class MainActivity : BaseActivity(), MainActivityLogic.ActivityProvider {

    lateinit var mainActivityLogic: MainActivityLogic

    private val FRAGMNET = "fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("TAG", "onCreate: ")
        mainActivityLogic = MainActivityLogic(this, savedInstanceState)
        PushAgent.getInstance(this).onAppStart()
        DataBus.with<String>("stickyData").observeSticky(this, false) {

        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainActivityLogic.onSaveInstanceState(outState)
        lifecycleScope.launchWhenCreated {
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
    }


    override fun onResume() {

        super.onResume()
        Log.e("TAG", "onResume: ")
        var fragment = supportFragmentManager.findFragmentByTag("tag")
        if (fragment == null) {
            fragment = MyF();
            supportFragmentManager.beginTransaction().add(fragment, "tag").commitAllowingStateLoss()
        }
        val viewModel = ViewModelProvider(this).get(HiViewModel::class.java)


        viewModel.loadInitData().observe(this) {

        }
    }

    override fun onStart() {
        super.onStart()
        Log.e("TAG", "onStart: ")
    }

    override fun onRestart() {
        super.onRestart()
        Log.e("TAG", "onRestart: ")
    }

    override fun onPause() {
        super.onPause()
        Log.e("TAG", "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.e("TAG", "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG", "onDestroy: ")
    }
}