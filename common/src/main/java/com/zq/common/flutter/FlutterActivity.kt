package com.zq.common.flutter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zq.common.R


@Route(path = "/flutter/main")
class FlutterActivity : AppCompatActivity() {
    @JvmField
    @Autowired
    var moduleName: String? = null
    var flutterFragment: MFlutterFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.activity_flutter)
        initFragment()
    }

    private fun initFragment() {
        flutterFragment = MFlutterFragment()
        supportFragmentManager.beginTransaction().add(R.id.root_view, flutterFragment!!).commit()
    }


    inner class MFlutterFragment : FlutterFragment() {
        override val moduleName: String
            get() = this@FlutterActivity.moduleName!!

        override fun onDestroy() {
            super.onDestroy()
            FlutterCacheManager.instances?.destroyCached(moduleName)
        }

    }
}