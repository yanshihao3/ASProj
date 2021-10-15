package com.zq.asproj.http.api

import com.zq.asproj.model.CourseNotice
import com.zq.asproj.model.UserProfile
import com.zq.hilibrary.restful.HiCall
import com.zq.hilibrary.restful.annotation.Filed
import com.zq.hilibrary.restful.annotation.GET
import com.zq.hilibrary.restful.annotation.POST


interface AccountApi {

    @POST("user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ): HiCall<String>


    @POST("user/registration")
    fun register(
        @Filed("userName") userName: String,
        @Filed("password") password: String,
        @Filed("imoocId") imoocId:
        String, @Filed("orderId") orderId: String
    ): HiCall<String>


    @GET("user/profile")
    fun profile(): HiCall<UserProfile>


    @GET("notice")
    fun notice(): HiCall<CourseNotice>
}