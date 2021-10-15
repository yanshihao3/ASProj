package com.zq.asproj.account

import android.app.Application
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.zq.asproj.LoginActivity
import com.zq.asproj.http.api.AccountApi
import com.zq.asproj.model.UserProfile
import com.zq.common.http.ApiFactory
import com.zq.common.utils.SPUtil
import com.zq.hilibrary.cache.Storage
import com.zq.hilibrary.executor.YExecutor
import com.zq.hilibrary.restful.HiCallback
import com.zq.hilibrary.restful.HiResponse
import com.zq.hilibrary.util.AppGlobals
import com.zq.hilibrary.util.MainHandler
import java.lang.ref.WeakReference

object AccountManager {

    private val lock = Any()

    private var userProfile: UserProfile? = null
    private var boardingPass: String? = null
    private val KEY_USER_PROFILE = "user_profile"
    private val KEY_BOARDING_PASS = "boarding_pass"

    private val loginLiveData = MutableLiveData<Boolean>()
    private val profileLiveData = MutableLiveData<UserProfile?>()


    @Volatile
    private var isFetching = false

    fun login(context: Context? = AppGlobals.get(), observer: Observer<Boolean>) {
        if (context is LifecycleOwner) {
            loginLiveData.observe(context, observer)
        } else {
            loginLiveData.observeForever(WeakObserver(observer))
        }

        val intent = Intent(context, LoginActivity::class.java)
        if (context is Application) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (context == null) {
            return
        }
        context.startActivity(intent)
    }

    fun loginSuccess(boardingPass: String) {
        SPUtil.putString(KEY_BOARDING_PASS, boardingPass)
        this.boardingPass = boardingPass
        loginLiveData.value = true
    }

    fun getBoardingPass(): String? {
        if (TextUtils.isEmpty(boardingPass)) {
            boardingPass = SPUtil.getString(KEY_BOARDING_PASS)
        }
        return boardingPass
    }

    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(getBoardingPass())
    }

    @Synchronized
    fun getUserProfile(
        lifecycleOwner: LifecycleOwner?,
        observer: Observer<UserProfile?>,
        onlyCache: Boolean = false
    ) {
        if (lifecycleOwner == null) {
            //没有提供lifecycleowner ，则包装成WeakReference
            profileLiveData.observeForever(WeakObserver(observer))
        } else {
            profileLiveData.observe(lifecycleOwner, observer)
        }

        if (isFetching) return
        isFetching = true

        if (onlyCache) {
            synchronized(lock) {
                if (userProfile != null) {
                    MainHandler.post(Runnable {
                        this.profileLiveData.value = userProfile
                    })
                    return
                }
            }
        }

        ApiFactory.create(AccountApi::class.java).profile()
            .enqueue(object : HiCallback<UserProfile> {
                override fun onSuccess(response: HiResponse<UserProfile>) {
                    if (response.successful() && response.data != null) {
                        userProfile = response.data
                        YExecutor.execute(runnable = Runnable {
                            Storage.saveCache(KEY_USER_PROFILE, userProfile)
                            isFetching = false
                        })
                        profileLiveData.value = userProfile
                    } else {
                        profileLiveData.value = null
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    isFetching = false
                    profileLiveData.value = null
                }
            })
    }

    class WeakObserver<T>(delegate: Observer<T>) : Observer<T> {
        private val weakRef = WeakReference(delegate)
        override fun onChanged(t: T) {
            weakRef.get()?.onChanged(t)
        }
    }
}