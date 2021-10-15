package com.zq.asproj.http.api

import com.zq.asproj.model.Favorite
import com.zq.hilibrary.restful.HiCall
import com.zq.hilibrary.restful.annotation.POST
import com.zq.hilibrary.restful.annotation.Path


interface FavoriteApi {

    @POST("favorites/{goodsId}")
    fun favorite(@Path("goodsId") goodsId: String): HiCall<Favorite>
}