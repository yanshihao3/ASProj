package com.zq.ability

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.umeng.commonsdk.statistics.common.DeviceConfig
import com.zq.ability.analyse.AnalyseUtil
import com.zq.ability.pay.alipay.AliPayHelper
import com.zq.ability.pay.alipay.PayResult
import com.zq.ability.pay.wxpay.WxPayHelper
import com.zq.ability.push.IPushMessageHandler
import com.zq.ability.push.PushInitialization
import com.zq.ability.scan.ScanActivity
import com.zq.ability.share.ShareBundle
import com.zq.ability.share.ShareManager
import com.zq.hilibrary.util.AppGlobals
import com.zq.hilibrary.util.ViewUtil

/**
 * @program: ASProj
 * @description:
 * @author: 闫世豪
 * @create: 2021-11-12 15:33
 */
object Ability {

    private val scanResultLiveData = MutableLiveData<String>()

    fun init(
        application: Application, channel: String,
        iPushMessageHandler: IPushMessageHandler? = null
    ) {
        if (ViewUtil.inMainProcess(application)) {
            PushInitialization.init(application, channel, iPushMessageHandler)
            AnalyseUtil.init(application, channel)
        }
    }

    /**
     * 唤起分享面板
     */
    fun share(context: Context, shareBundle: ShareBundle) {
        ShareManager.share(context, shareBundle)
    }

    /**
     * 打开扫码页,结果通过observer来接收
     */
    fun openScanActivity(activity: Activity, observer: Observer<String>) {
        if (activity is LifecycleOwner) {
            scanResultLiveData.observe(activity, observer)
        }
        activity.startActivity(Intent(activity, ScanActivity::class.java))
    }

    internal fun onScanResult(scanResult: String) {
        scanResultLiveData.postValue(scanResult)
        scanResultLiveData.value = null
    }

    fun traceEvent(eventId: String, values: Map<String, Any>) {
        AnalyseUtil.traceEvent(AppGlobals.get()!!, eventId, values)
    }

    /**
     * 是用来获取设备的唯一ID 和mac
     */
    fun getTestDeviceInfo(context: Context?): Array<String?>? {
        val deviceInfo = arrayOfNulls<String>(2)
        try {
            if (context != null) {
                deviceInfo[0] = DeviceConfig.getDeviceIdForGeneral(context)
                deviceInfo[1] = DeviceConfig.getMac(context)
            }
        } catch (e: Exception) {
        }
        return deviceInfo
    }

    fun aliPay(activity: Activity, orderInfo: String, observer: Observer<PayResult>) {
        AliPayHelper.pay(activity, orderInfo, observer)
    }

    private val wxPayResultLiveData=MutableLiveData<Int>()
    fun wxPay(activity: Activity, parameters: HashMap<String,String>, observer: Observer<Int>) {
        if (activity is FragmentActivity) {
            wxPayResultLiveData.observe(activity,observer)
        }
        WxPayHelper.pay(parameters)
    }

    internal fun postWXPayResult(result: Int) {
        wxPayResultLiveData.postValue(result)
        wxPayResultLiveData.value=null/*清空*/
    }

}
