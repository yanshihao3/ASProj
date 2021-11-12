package com.zq.ability

import android.app.Application
import com.zq.ability.push.IPushMessageHandler
import com.zq.ability.push.PushInitialization

/**
 * @program: ASProj
 * @description:
 * @author: 闫世豪
 * @create: 2021-11-12 15:33
 */
object Ability {
    fun init(
        application: Application, channel: String,
        iPushMessageHandler: IPushMessageHandler? = null
    ) {
        PushInitialization.init(application, channel, iPushMessageHandler)
    }
}
