package com.zq.ability.share

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.tencent.connect.share.QQShare
import com.tencent.connect.share.QQShare.SHARE_TO_QQ_TYPE_DEFAULT
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import com.zq.hilibrary.util.AppGlobals
import com.zq.hilibrary.util.ViewUtil


object ShareManager {
    const val CHANNEL_WX_FRIEND = "com.tencent.mm.ui.tools.ShareImgUI"
    const val CHANNEL_WX_CIRCLE = "com.tencent.mm.ui.tools.ShareToTimeLineUI"
    const val CHANNEL_WX_FAVORITE = "com.tencent.mm.ui.tools.AddFavoriteUI"
    val xWxapi = WXAPIFactory.createWXAPI(AppGlobals.get(), ShareConstants.WX_SHARE_APP_KEY, true)

    /**
     * channels 是调用方想要分享面板展示出来的渠道
     * 但是此时 某些渠道（QQ）就没有安装，那么我们需要查询本机已安装的应用列表
     * 和channels 做个过滤
     */
    fun share(context: Context, shareBundle: ShareBundle) {
        val localChannels = queryLocalChannels(context, shareBundle.type)
        val pm = context.packageManager
        val shareChannels = arrayListOf<ResolveInfo>()
        if (shareBundle.channels != null) {
            for (localChannel in localChannels) {
                val loadLabel = localChannel.loadLabel(pm)
                for (channel in shareBundle.channels!!) {
                    if (TextUtils.equals(channel, loadLabel)) {
                        shareChannels.add(localChannel)
                    }
                }
            }
        } else {
            shareChannels.addAll(localChannels)
        }

        val shareDialog = ShareDialog(context)
        shareDialog.setChannels(shareChannels, object : ShareDialog.onShareChannelClickListener {
            override fun onClickChannel(resolveInfo: ResolveInfo) {
                share2Channel(context, resolveInfo, shareBundle)
            }
        })
        shareDialog.show()
    }

    /**
     * 查询本地可以分享的渠道
     */
    private fun queryLocalChannels(context: Context, shareType: String): List<ResolveInfo> {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = shareType

        val resolveInfos = arrayListOf<ResolveInfo>()
        val pm = context.packageManager
        val activities = pm.queryIntentActivities(intent, 0)
        for (activity in activities) {
            val packageName = activity.activityInfo.packageName
            if (TextUtils.equals(packageName, "com.tencent.mm") || TextUtils.equals(
                    packageName,
                    "com.tencent.mobileqq"
                )
            ) {

            }
        }

        return resolveInfos
    }


    private fun share2Channel(
        context: Context,
        resolveInfo: ResolveInfo,
        shareBundle: ShareBundle
    ) {
        val activityName = resolveInfo.activityInfo.name
        when (activityName) {
            "CHANNEL_QQ_FRIEND" -> {
                share2QQFriend(context, shareBundle)
            }
            CHANNEL_WX_FRIEND -> {
                share2Wx(context, shareBundle, SendMessageToWX.Req.WXSceneSession)
            }
            CHANNEL_WX_CIRCLE -> {
                share2Wx(context, shareBundle, SendMessageToWX.Req.WXSceneTimeline)
            }
            CHANNEL_WX_FAVORITE -> {
                share2Wx(context, shareBundle, SendMessageToWX.Req.WXSceneFavorite)
            }
            else -> showToast(context, "微信分享请查看feature/wx_share分支代码")
        }
    }

    private fun share2Wx(context: Context, shareBundle: ShareBundle, wxSceneSession: Int) {
        val webPage = WXWebpageObject()
        webPage.webpageUrl = shareBundle.targetUrl
        val msg = WXMediaMessage(webPage)
        msg.title = shareBundle.title
        msg.description = shareBundle.title
        msg.thumbData = shareBundle.thumbData
        val req = SendMessageToWX.Req()
        req.scene = wxSceneSession
        req.message = msg
        req.transaction = System.currentTimeMillis().toString()
        xWxapi.sendReq(req)
    }

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun share2QQFriend(
        context: Context,
        shareBundle: ShareBundle
    ) {
        val params = Bundle()
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, SHARE_TO_QQ_TYPE_DEFAULT)
        params.putString(QQShare.SHARE_TO_QQ_TITLE, shareBundle.title)
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, shareBundle.summary)
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, shareBundle.targetUrl)
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, shareBundle.thumbUrl)
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, shareBundle.appName)

        val mTencent = Tencent.createInstance(ShareConstants.QQ_SHARE_APP_ID, context)
        val activity = ViewUtil.findActivity(context)
            ?: throw RuntimeException("context must typeof Activity.")
        mTencent.shareToQQ(activity, params, object : IUiListener {
            override fun onComplete(p0: Any?) {
                showToast(context, "分享成功")
            }

            override fun onCancel() {
                showToast(context, "取消分享")
            }

            override fun onWarning(p0: Int) {

            }

            override fun onError(p0: UiError?) {
                p0?.apply {
                    showToast(context, p0.errorMessage)
                }
            }

        })
    }
}