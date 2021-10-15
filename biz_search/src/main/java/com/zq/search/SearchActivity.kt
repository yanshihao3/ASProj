package com.zq.search

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zq.service.ILoginService


@Route(path = "/search/activity")
class SearchActivity : AppCompatActivity() {
    @Autowired
    lateinit var loginService: ILoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        ARouter.getInstance().inject(this)

        Log.e("TAG", "onCreate: 测试服务=== ${loginService.isLogin()}")

    }
}