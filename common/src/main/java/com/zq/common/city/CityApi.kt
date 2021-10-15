package com.zq.common.city

import com.zq.hilibrary.restful.HiCall
import com.zq.hilibrary.restful.annotation.Filed
import com.zq.hilibrary.restful.annotation.GET


internal interface CityApi {
    @GET("cities")
    fun listCities(): HiCall<CityModel>


    @GET("xzqh/query")
    fun getCity(@Filed("key") key: String = "ed6f40bc3e06abf4fdcfec0d35ebaa19",
                @Filed("fid") fid: String): HiCall<CityBean>
}
