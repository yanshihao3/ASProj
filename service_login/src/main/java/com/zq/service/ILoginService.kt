package com.zq.service

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-12 15:28
 **/
interface ILoginService :IProvider {

    fun login(context: Context?, observer: Observer<Boolean>)

    fun isLogin():Boolean

    fun getUserProfile(lifecycleOwner: LifecycleOwner?,
                       observer: Observer<UserProfile?>,
                       onlyCache: Boolean = false)

    fun getBoardingPass(): String?
}