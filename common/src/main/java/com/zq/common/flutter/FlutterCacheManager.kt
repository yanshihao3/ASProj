package com.zq.common.flutter

import android.content.Context
import android.os.Handler
import android.os.Looper
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.embedding.engine.loader.FlutterLoader
import io.flutter.view.FlutterMain

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-18 16:52
 **/
/**
 * @功能描述：优化flutter 加载速度
 * @备注：1、预加载 2、flutter 不同dart入口
 */
class FlutterCacheManager private constructor() {
    private fun initFlutterEngine(context: Context, moduleName: String): FlutterEngine {
        val flutterEngine = FlutterEngine(context)
        FlutterBridge.init(flutterEngine)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint(
                FlutterMain.findAppBundlePath(),
                moduleName
            )
        )
        FlutterEngineCache.getInstance().put(moduleName, flutterEngine)
        return flutterEngine
    }

    /**
     * 预加载 flutter
     */

    public fun preLoad(context: Context) {
        // 在系统空间的时候的 初始化 flutter 引擎
        Looper.myQueue().addIdleHandler {
            initFlutterEngine(context, MODULE_NAME_FAVORITE)
            initFlutterEngine(context, MODULE_NAME_RECOMMEND)
            false
        }
    }

    //获取预加载的flutter
    fun getCachedFlutterEngine(moduleName: String, context: Context?): FlutterEngine {
        var flutterEngine = FlutterEngineCache.getInstance()[moduleName]
        if (flutterEngine == null && context != null) {
            flutterEngine = initFlutterEngine(context, moduleName)
        }
        return flutterEngine!!
    }

    fun hasCached(moduleName: String): Boolean {
        return FlutterEngineCache.getInstance().contains(moduleName)
    }

    fun destroyCached(moduleName: String) {
        FlutterEngineCache.getInstance()[moduleName]?.apply {
            destroy()
        }
        FlutterEngineCache.getInstance().remove(moduleName)
    }

    fun preLoadDartVM(context: Context) {
        val settings = FlutterLoader.Settings()
        FlutterLoader.getInstance().startInitialization(context, settings)
        val mainHandler = Handler(Looper.getMainLooper())
        FlutterLoader.getInstance()
            .ensureInitializationCompleteAsync(context, arrayOf(), mainHandler) {

            }
    }

    companion object {

        const val MODULE_NAME_FAVORITE = "main"
        const val MODULE_NAME_RECOMMEND = "recommend"

        @JvmStatic
        val instances: FlutterCacheManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            FlutterCacheManager()
        }
    }
}