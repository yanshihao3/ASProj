package com.zq.ability.push

import android.app.Application
import android.content.Context
import android.util.Log
import com.umeng.commonsdk.UMConfigure
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.api.UPushRegisterCallback
import com.umeng.message.entity.UMessage
import org.android.agoo.huawei.HuaWeiRegister
import org.android.agoo.xiaomi.MiPushRegistar
import org.json.JSONObject


/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-11-12 15:33
 **/
object PushInitialization {

    const val TAG = "PushInitialization"

    private var iPushMessageHandler: IPushMessageHandler? = null

    fun init(
        application: Application,
        channel: String,
        iPushMessageHandler: IPushMessageHandler? = null
    ) {
        this.iPushMessageHandler = iPushMessageHandler
        preInit(application, channel)
        initUmengPushSdk(application, channel)
        registerDeviceChannel(application)
    }

    /**
     * 预初始化
     */
    fun preInit(context: Application, channel: String) {
        //解决厂商通知点击时乱码等问题
        PushAgent.setup(context, PushConstants.APP_KEY, PushConstants.MESSAGE_SECRET)
        UMConfigure.preInit(context, PushConstants.APP_KEY, channel)
    }

    private fun initUmengPushSdk(application: Application, channel: String) {
        // 基础组件包提供的初始化函数，应用配置信息：http://message.umeng.com/list/apps
        // 参数一：上下文context；
        // 参数二：应用申请的Appkey；
        // 参数三：发布渠道名称；
        // 参数四：设备类型，UMConfigure.DEVICE_TYPE_PHONE：手机；UMConfigure.DEVICE_TYPE_BOX：盒子；默认为手机
        // 参数五：Push推送业务的secret，填写Umeng Message Secret对应信息
        UMConfigure.init(
            application, PushConstants.APP_KEY, channel,
            UMConfigure.DEVICE_TYPE_PHONE, PushConstants.MESSAGE_SECRET
        )
        //获取推送实例
        val pushAgent = PushAgent.getInstance(application)

        //TODO:需修改为您app/src/main/AndroidManifest.xml中package值
        pushAgent.resourcePackageName = "com.zq.asproj"
        //推送设置
        pushSetting(application)
        //注册推送服务，每次调用register方法都会回调该接口
        pushAgent.register(object : UPushRegisterCallback {
            override fun onSuccess(deviceToken: String) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG, "deviceToken --> $deviceToken")
                iPushMessageHandler?.onRegisterSuccess(deviceToken)
            }

            override fun onFailure(errCode: String, errDesc: String) {
                iPushMessageHandler?.onRegisterFailed(errCode, errDesc)
                Log.e(TAG, "register failure：--> code:$errCode,desc:$errDesc")
            }
        })
    }


    //推送设置
    private fun pushSetting(context: Context) {
        val pushAgent = PushAgent.getInstance(context)

        //设置通知栏显示通知的最大个数（0～10），0：不限制个数
        pushAgent.displayNotificationNumber = 0

        //推送消息处理
        val msgHandler: UmengMessageHandler = object : UmengMessageHandler() {
            //处理通知栏消息
            override fun dealWithNotificationMessage(context: Context?, msg: UMessage) {
                if (iPushMessageHandler != null) {
                    iPushMessageHandler!!.dealWithNotificationMessage(context, JSONObject())
                } else {
                    super.dealWithNotificationMessage(context, msg)
                }
            }


            //处理透传消息
            override fun dealWithCustomMessage(context: Context?, msg: UMessage) {
                if (iPushMessageHandler != null) {
                    iPushMessageHandler!!.dealWithCustomMessage(context, JSONObject())
                } else {
                    super.dealWithCustomMessage(context, msg)
                }
            }
        }
        pushAgent.messageHandler = msgHandler

        //推送消息点击处理
        val notificationClickHandler: UmengNotificationClickHandler =
            object : UmengNotificationClickHandler() {
                override fun dealWithCustomAction(p0: Context?, p1: UMessage?) {
                    if (iPushMessageHandler != null) {
                        //需要自己按照应用 约定的数据结构 去解析UMessage---->JSONOBJECT
                        iPushMessageHandler!!.dealWithCustomAction(p0, JSONObject())
                    } else {
                        super.dealWithCustomAction(p0, p1)
                    }
                }
            }
        pushAgent.notificationClickHandler = notificationClickHandler
    }

    /**
     * 注册设备推送通道（小米、华为等设备的推送）
     */
    private fun registerDeviceChannel(context: Context) {
        //小米通道，填写您在小米后台APP对应的xiaomi id和key
        MiPushRegistar.register(context, PushConstants.MI_ID, PushConstants.MI_KEY)
        //华为，注意华为通道的初始化参数在minifest中配置
        HuaWeiRegister.register(context.applicationContext as Application)
        /* //魅族，填写您在魅族后台APP对应的app id和key
         MeizuRegister.register(context, PushConstants.MEI_ZU_ID, PushConstants.MEI_ZU_KEY)
         //OPPO，填写您在OPPO后台APP对应的app key和secret
         OppoRegister.register(context, PushConstants.OPPO_KEY, PushConstants.OPPO_SECRET)
         //vivo，注意vivo通道的初始化参数在minifest中配置
         VivoRegister.register(context)*/
    }

    fun onOEMPush(message: String) {
        iPushMessageHandler?.dealWithCustomMessage(null, JSONObject(message))
    }
}