package com.zq.common.city

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zq.common.http.ApiFactory
import com.zq.hilibrary.cache.Storage
import com.zq.hilibrary.executor.YExecutor
import com.zq.hilibrary.log.YLog
import com.zq.hilibrary.restful.HiCallback
import com.zq.hilibrary.restful.HiResponse
import com.zq.ui.cityselector.*

import java.util.concurrent.atomic.AtomicBoolean

/**
 * 获取城市数据,优先本地缓存，否则接口请求再更新本地缓存，同时一刻多次调用只有一次生效
 *
 * 内存常驻，所以可以考虑手动清理LiveData.value
 */
object CityMgr {
    private const val KEY_CITY_DATA_SET = "city_data_set"
    private val liveData = MutableLiveData<List<Province>?>()

    //是否正在加载数据...，同一时刻只有一个线程，或者一处能够发起数据的加载
    private val isFetching = AtomicBoolean(false)

    fun getCityData(): LiveData<List<Province>?> {
        //发送过一次数据之后，这个数据List<Province> 会存储在value
        if (isFetching.compareAndSet(false, true) && liveData.value == null) {
            getCache { cache ->
                if (cache != null) {
                    liveData.postValue(cache)
                    isFetching.set(false)
                } else {
                    fetchRemote { remote ->
                        liveData.postValue(remote)
                        isFetching.set(false)
                    }
                }
            }
        }
        return liveData
    }

    private fun getCache(callback: (List<Province>?) -> Unit) {
        YExecutor.execute(runnable = Runnable {
            val cache = Storage.getCache<List<Province>?>(KEY_CITY_DATA_SET)
            callback(cache)
        })
    }

    //持久化到本地
    private fun saveCache(groupList: List<Province>?) {
        if (groupList.isNullOrEmpty()) return
        YExecutor.execute(runnable = Runnable {
            Storage.saveCache(KEY_CITY_DATA_SET, groupList)
        })
    }

    private fun fetchRemote(callback: (List<Province>?) -> Unit) {
        ApiFactory.create(CityApi::class.java).listCities().enqueue(object : HiCallback<CityModel> {
            override fun onSuccess(response: HiResponse<CityModel>) {
                if (response.data?.list?.isNullOrEmpty() == false) {
                    //分组处理之后的数据，三级数据结构
                    groupByProvince(response.data!!.list) { groupList ->
                        saveCache(groupList)
                        callback(groupList)
                    }
                } else {
                    YLog.e("response data list is empty or null")
                    callback(null)
                }
            }

            override fun onFailed(throwable: Throwable) {
                if (!TextUtils.isEmpty(throwable.message)) YLog.e(throwable.message)
                callback(null)
            }
        })
    }

    //对数据进行分组，生成三级数据结构
    private fun groupByProvince(list: List<District>, callback: (List<Province>?) -> Unit) {
        //是为了收集所有的省，同时也是为了TYPE_CITY，快速找到自己所在的province对象
        val provinceMaps = hashMapOf<String, Province>()
        //是为了TYPE_DISTRICT 快速找到自己所在的city对象
        val cityMaps = hashMapOf<String, City>()
        YExecutor.execute(runnable = Runnable {
            for (element in list) {
                when (element.type) {
                    TYPE_COUNTRY -> {
                    }
                    TYPE_PROVINCE -> {
                        val province = Province()
                        District.copyDistrict(element, province)
                        provinceMaps[element.id!!] = province
                    }
                    TYPE_CITY -> {
                        val city = City()
                        District.copyDistrict(element, city)
                        val province = provinceMaps[element.pid]
                        province?.cities?.add(city)
                        cityMaps[element.id!!] = city
                    }
                    TYPE_DISTRICT -> {
                        val city = cityMaps[element.pid]
                        city?.districts?.add(element)
                    }
                }
                callback(ArrayList(provinceMaps.values))
            }
        })
    }

}