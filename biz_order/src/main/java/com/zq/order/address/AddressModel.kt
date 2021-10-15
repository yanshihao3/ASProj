package com.zq.order.address

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class AddressModel(val total: Int, val list: List<Address>)

@Parcelize
data class Address(
    var province: String,
    var city: String,
    var area: String,
    var detail: String,
    var `receiver`: String,
    var phoneNum: String,
    val id: String,
    val uid: String
) : Parcelable