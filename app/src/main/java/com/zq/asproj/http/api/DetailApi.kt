package com.zq.asproj.http.api

import com.zq.asproj.model.DetailModel
import com.zq.hilibrary.restful.HiCall
import com.zq.hilibrary.restful.annotation.GET
import com.zq.hilibrary.restful.annotation.Path

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-08 11:11
 **/
interface DetailApi {

    @GET("goods/detail/{id}")
    fun queryDetail(@Path("id") goodId: String): HiCall<DetailModel>
}