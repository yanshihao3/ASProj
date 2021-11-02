package com.zq.common.flutter

import com.alibaba.android.arouter.launcher.ARouter
import com.zq.common.ext.showToast
import com.zq.hilibrary.util.ActivityManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-18 18:33
 **/
class FlutterBridge private constructor() : IBridge<Any, MethodChannel.Result>,

    MethodChannel.MethodCallHandler {

    private var methodChannels = mutableListOf<MethodChannel>()

    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            FlutterBridge()
        }

        @JvmStatic
        fun init(flutterEngine: FlutterEngine) {
            val methodChannel = MethodChannel(flutterEngine.dartExecutor, "FlutterBridge")
            methodChannel.setMethodCallHandler(instance)
            instance.apply {
                /*每个engine单独注册channel*/
                methodChannels.add(methodChannel)
            }
        }
    }

    /**
     * native 向 flutter发送消息
     */
    fun fire(method: String, arguments: Any?) {
        methodChannels.forEach {
            it.invokeMethod(method, arguments)
        }
    }

    fun fire(method: String, arguments: Any?, callBack: MethodChannel.Result) {
        methodChannels.forEach {
            it.invokeMethod(method, arguments, callBack)
        }
    }

    override fun onBack(p: Any?) {
        if (ActivityManager.instance.topActivity is FlutterActivity) {
            (ActivityManager.instance.topActivity as FlutterActivity).onBackPressed()
        }
    }

    override fun goToNative(p: Any) {
        if (p is Map<*, *>) {
            val action = p["action"]
            showToast(action.toString())
            if (action == "goToDetail") {
                val goodsId = p["goodsId"]
                ARouter.getInstance().build("/detail/main")
                    .withString("goodsId", goodsId as String?).navigation()
            } else if (action == "onBack") {

            } else if (action == "goToLogin") {
                ARouter.getInstance().build("/account/login").navigation()
            }
        }
    }

    override fun getHeaderParams(callback: MethodChannel.Result) {
        callback!!.success(
            mapOf(
                "boarding-pass" to "",
                "auth-token" to "",
            )
        )
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "onBack" -> onBack(call.arguments)
            "getHeaderParams" -> getHeaderParams(result)
            "goToNative" -> goToNative(call.arguments)
            else -> result.notImplemented()
        }
    }
}