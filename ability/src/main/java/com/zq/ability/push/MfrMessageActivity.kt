package com.zq.ability.push

import android.content.Intent
import com.umeng.message.UmengNotifyClickActivity
import org.android.agoo.common.AgooConstants

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-11-12 16:03
 **/
class MfrMessageActivity : UmengNotifyClickActivity() {

    override fun onMessage(intent: Intent?) {
        super.onMessage(intent)
        val body: String? = intent?.getStringExtra(AgooConstants.MESSAGE_BODY)
        body?.apply {
            PushInitialization.onOEMPush(this)
        }
    }
}