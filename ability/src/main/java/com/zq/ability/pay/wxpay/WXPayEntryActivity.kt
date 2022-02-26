package com.zq.ability.pay.wxpay

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zq.ability.Ability

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-11-15 16:11
 **/
class WXPayEntryActivity : AppCompatActivity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null
    private val mPrice: String? = null
    private var mName: kotlin.String? = null
    private var mTel: kotlin.String? = null
    private var mAddress: kotlin.String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = WXAPIFactory.createWXAPI(this, WxPayConstants.WX_PAY_APP_ID)
        api?.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api?.handleIntent(intent, this)
    }

    override fun onReq(p0: BaseReq?) {

    }

    override fun onResp(resp: BaseResp?) {
        if (resp?.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Ability.postWXPayResult(resp.errCode)
            finish()
        }
    }
}