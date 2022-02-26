package com.zq.ability.pay.alipay

import android.app.Activity
import androidx.lifecycle.Observer
import com.alipay.sdk.app.PayTask
import com.zq.hilibrary.executor.YExecutor

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-11-15 15:57
 **/
object AliPayHelper {
    fun pay(activity: Activity, orderInfo: String, observer: Observer<PayResult>) {
        YExecutor.execute(runnable = Runnable {
            val aliPayTask = PayTask(activity)

            val resultMap = aliPayTask.payV2(orderInfo, true)

            val payResult = PayResult(resultMap)

            observer.onChanged(payResult)
        })
    }
}