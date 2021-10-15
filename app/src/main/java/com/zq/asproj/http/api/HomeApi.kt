package com.zq.asproj.http.api

import com.zq.asproj.model.HomeModel
import com.zq.asproj.model.TabCategory
import com.zq.hilibrary.restful.HiCall
import com.zq.hilibrary.restful.annotation.Filed
import com.zq.hilibrary.restful.annotation.GET
import com.zq.hilibrary.restful.annotation.POST
import com.zq.hilibrary.restful.annotation.Path


interface HomeApi {
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>


    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @Path("categoryId") categoryId: String,
        @Filed("pageIndex") pageIndex: Int,
        @Filed("pageSize") pageSize: Int
    ): HiCall<HomeModel>
}