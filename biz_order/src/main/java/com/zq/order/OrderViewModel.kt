package com.zq.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zq.common.http.ApiFactory
import com.zq.hilibrary.restful.HiCallback
import com.zq.hilibrary.restful.HiResponse
import com.zq.order.address.Address
import com.zq.order.address.AddressApi
import com.zq.order.address.AddressModel

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-13 13:35
 **/
class OrderViewModel : ViewModel() {
    fun queryMainAddress(): LiveData<Address?> {
        val liveData = MutableLiveData<Address?>()
        ApiFactory.create(AddressApi::class.java).queryAddress(1, 1)
            .enqueue(object : HiCallback<AddressModel> {
                override fun onSuccess(response: HiResponse<AddressModel>) {
                    val list = response.data?.list
                    val firstElement = if (list?.isNotEmpty() == true) list[0] else null
                    liveData.postValue(firstElement)
                }

                override fun onFailed(throwable: Throwable) {
                    liveData.postValue(null)
                }
            })
        return liveData
    }
}