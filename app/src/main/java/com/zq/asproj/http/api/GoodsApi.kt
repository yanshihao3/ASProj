package com.zq.asproj.http.api

import com.zq.asproj.model.GoodsList
import com.zq.hilibrary.restful.HiCall
import com.zq.hilibrary.restful.annotation.Filed
import com.zq.hilibrary.restful.annotation.GET
import com.zq.hilibrary.restful.annotation.POST
import com.zq.hilibrary.restful.annotation.Path


interface GoodsApi {
    @GET("goods/goods/{categoryId}")
    fun queryCategoryGoodsList(
        @Path("categoryId") categoryId: String,
        @Filed("subcategoryId") subcategoryId: String,
        @Filed("pageSize") pageSize: Int,
        @Filed("pageIndex") pageIndex: Int
    ): HiCall<GoodsList>
}