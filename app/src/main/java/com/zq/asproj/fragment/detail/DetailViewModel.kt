package com.zq.asproj.fragment.detail

import android.text.TextUtils
import androidx.lifecycle.*
import com.alibaba.android.arouter.BuildConfig
import com.zq.asproj.http.api.DetailApi
import com.zq.asproj.http.api.FavoriteApi
import com.zq.asproj.model.DetailModel
import com.zq.asproj.model.Favorite
import com.zq.common.http.ApiFactory
import com.zq.hilibrary.restful.HiCallback
import com.zq.hilibrary.restful.HiResponse

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-08 13:55
 **/
class DetailViewModel(val goodsId: String?) : ViewModel() {

    fun queryDetailData(): LiveData<DetailModel?> {
        val pageData = MutableLiveData<DetailModel?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(DetailApi::class.java).queryDetail(goodsId!!)
                .enqueue(object : HiCallback<DetailModel> {
                    override fun onSuccess(response: HiResponse<DetailModel>) {
                        if (response.successful() && response.data != null) {
                            pageData.postValue(response.data)
                        } else {
                            pageData.postValue(null)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        pageData.postValue(null)
                        if (BuildConfig.DEBUG) {
                            throwable.printStackTrace()
                        }
                    }

                })
        }
        return pageData
    }

    fun toggleFavorite(): LiveData<Boolean?> {
        val toggleFavoriteData = MutableLiveData<Boolean?>()
        if (!TextUtils.isEmpty(goodsId)) {
            ApiFactory.create(FavoriteApi::class.java).favorite(goodsId!!)
                .enqueue(object : HiCallback<Favorite> {
                    override fun onSuccess(response: HiResponse<Favorite>) {
                        toggleFavoriteData.postValue(response.data?.isFavorite)
                    }

                    override fun onFailed(throwable: Throwable) {
                        toggleFavoriteData.postValue(null)
                    }
                })
        }
        return toggleFavoriteData
    }

    companion object {
        fun get(goodsId: String?, viewModelStoreOwner: ViewModelStoreOwner): DetailViewModel {
            return ViewModelProvider(viewModelStoreOwner, DetailViewModelFactory(goodsId)).get(
                DetailViewModel::class.java
            )
        }
    }
}

class DetailViewModelFactory(val goodsId: String?) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(String::class.java).newInstance(goodsId)
        } catch (e: Exception) {
            throw  RuntimeException("Cannot create an instance of $modelClass", e)
        }
        return super.create(modelClass)
    }
}