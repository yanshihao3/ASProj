package com.zq.asproj.http.api

import com.zq.hilibrary.restful.HiCall
import com.zq.hilibrary.restful.annotation.Filed
import com.zq.hilibrary.restful.annotation.GET
import com.zq.hilibrary.restful.annotation.POST
import com.zq.hilibrary.restful.annotation.Path
import com.zq.asproj.model.CourseNotice
import com.zq.asproj.model.Subcategory
import com.zq.asproj.model.TabCategory
import com.zq.asproj.model.UserProfile

interface CategoryApi {
    @GET("category/categories")
    fun queryCategoryList(): HiCall<List<TabCategory>>


    @GET("category/subcategories/{categoryId}")
    fun querySubcategoryList(@Path("categoryId") categoryId: String): HiCall<List<Subcategory>>
}