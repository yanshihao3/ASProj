package com.zq.asproj

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.zq.asproj.logic.MainActivityLogic
import com.zq.hilibrary.util.DataBus


class MainActivity : AppCompatActivity(), MainActivityLogic.ActivityProvider {

    lateinit var mainActivityLogic: MainActivityLogic

    private val FRAGMNET = "fragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("TAG", "onCreate: ")
        mainActivityLogic = MainActivityLogic(this, savedInstanceState)

        DataBus.with<String>("stickyData").observeSticky(this, false) {

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainActivityLogic.onSaveInstanceState(outState)
        lifecycleScope.launchWhenCreated {


        }
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
        viewModel.loadInitData().observe(this){

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