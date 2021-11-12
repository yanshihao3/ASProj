package com.zq.debugtool

import android.content.Intent
import android.os.Process
import androidx.appcompat.app.AppCompatDelegate
import com.zq.common.utils.SPUtil
import com.zq.hilibrary.fps.FpsMonitor
import com.zq.hilibrary.util.AppGlobals
import com.zq.hilibrary.util.ViewUtil


/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-27 14:28
 **/
class DebugTools {


    fun buildVersion(): String {
        return "构建版本:$"
    }


    @Debug(name = "一键开启https降级", desc = "将继承http，可以使用抓包工具明文抓包")
    fun degrade2Http() {
        SPUtil.putBoolean("degrade_http", true)
        val context = AppGlobals.get()?.applicationContext ?: return
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        //得到了 启动页的intent
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        //杀掉当前进程，并主动启动新的 启动页 已完成重启的动作
        Process.killProcess(Process.myPid())
    }

    @Debug(name = "查看Crash日志", desc = "可一键分享")
    fun crashLog() {
        val context = AppGlobals.get()?.applicationContext ?: return
        val intent = Intent(context, CrashLogActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    @Debug(name = "FPS", "实时FPS")
    fun toggleFps() {
        FpsMonitor.toggle()
    }

    @Debug(name = "暗黑模式", "")
    fun toggleTheme() {
        if (ViewUtil.lightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }


}