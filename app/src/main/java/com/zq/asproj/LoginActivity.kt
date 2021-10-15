package com.zq.asproj

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.zq.ui.search.SearchView
import dagger.hilt.android.AndroidEntryPoint


@Route(path = "/account/login")
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<SearchView>(R.id.searchView).run {
            this.setHintText("请输入信息")
            this.setKeyWord("haha") {

            }
            this.setClearIconClickListener {
                Log.e("TAG", "onCreate: setClearIconClickListener")

            }

        }
    }

    override fun onRestart() {
        super.onRestart()

    }

    /**
     * 当配置变更，可以保存数据
     */
    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return super.onRetainCustomNonConfigurationInstance()
    }
}