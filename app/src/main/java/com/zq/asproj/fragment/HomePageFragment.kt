package com.zq.asproj.fragment

import android.os.Bundle
import com.zq.asproj.R
import com.zq.common.ui.component.BaseFragment


/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-16 15:11
 **/
class HomePageFragment : BaseFragment() {


    override fun getLayoutId(): Int {
        return R.layout.fragment_home_page
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        ApiFactory.create(HomeApi::class.java).queryTabList().enqueue(object :HiCallback<List<TabCategory>>{
//            override fun onSuccess(response: HiResponse<List<TabCategory>>) {
//
//            }
//
//            override fun onFailed(throwable: Throwable) {
//
//            }
//
//        })
    }
}