package com.zq.asproj

import androidx.lifecycle.*

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-29 13:28
 **/
class HiViewModel() : ViewModel() {

    val data = MutableLiveData<String>()

    fun loadInitData(): LiveData<String> {
        if (data.value == null) {
            data.value = "text"
            return data
        }
        return data
    }
}

