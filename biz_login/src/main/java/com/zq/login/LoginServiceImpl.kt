package com.zq.login

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.zq.service.ILoginService
import com.zq.service.UserProfile

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-12 15:40
 **/

@Route(path = "/service/login", name = "测试服务")
class LoginServiceImpl : ILoginService {
    override fun login(context: Context?, observer: Observer<Boolean>) {

    }

    override fun isLogin(): Boolean {
        return true
    }

    override fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean
    ) {

    }

    override fun getBoardingPass(): String? {
        return ""
    }

    override fun init(context: Context?) {

    }
}