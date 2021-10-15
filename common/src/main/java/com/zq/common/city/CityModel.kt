package com.zq.common.city

import com.zq.ui.cityselector.District


internal data class CityModel(val total: Int, val list: List<District>)

internal data class CityBean(
    val id: String,
    val name: String,
    val fid: Int,
    val level_id: Int
)