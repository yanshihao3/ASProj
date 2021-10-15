package com.zq.asproj

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-29 10:23
 **/

class MyF : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(Companion.TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e(Companion.TAG, "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        Log.e(Companion.TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.e(Companion.TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.e(Companion.TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.e(Companion.TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(Companion.TAG, "onDestroy")
    }

    companion object {
        private const val TAG = "MyF"
    }

}