package com.zq.common.flutter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.zq.common.R
import com.zq.common.ext.showToast
import com.zq.common.ui.component.BaseFragment
import com.zq.hilibrary.util.AppGlobals
import io.flutter.embedding.android.FlutterTextureView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-18 16:38
 **/
abstract class FlutterFragment : BaseFragment() {
    protected lateinit var flutterEngine: FlutterEngine
    protected var flutterView: FlutterView? = null

    abstract val moduleName: String

    init {
        flutterEngine =
            FlutterCacheManager.instances.getCachedFlutterEngine(moduleName, AppGlobals.get())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setTitle("")

    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_flutter
    }

    fun setTitle(title: String) {
        FlutterBridge.instance.fire("onRefresh","so easy",object :MethodChannel.Result{
            override fun success(result: Any?) {
                showToast(result.toString())
            }

            override fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?) {

            }

            override fun notImplemented() {

            }

        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (layoutView as ViewGroup).addView(createFlutterView(requireActivity()!!))

    }


    private fun createFlutterView(context: Context): FlutterView {
        val flutterTextureView = FlutterTextureView(requireActivity())
        flutterView = FlutterView(context, flutterTextureView)
        return flutterView!!
    }

    override fun onStart() {
        flutterView!!.attachToFlutterEngine(flutterEngine!!)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        flutterEngine!!.lifecycleChannel.appIsResumed()/*flutter>=1.17*/
    }

    override fun onPause() {
        super.onPause()
        flutterEngine!!.lifecycleChannel.appIsInactive()
    }

    override fun onStop() {
        super.onStop()
        flutterEngine!!.lifecycleChannel.appIsPaused()
    }

    override fun onDetach() {
        super.onDetach()
        flutterEngine!!.lifecycleChannel.appIsDetached()
    }

    override fun onDestroy() {
        super.onDestroy()
        flutterView?.detachFromFlutterEngine()
    }
}