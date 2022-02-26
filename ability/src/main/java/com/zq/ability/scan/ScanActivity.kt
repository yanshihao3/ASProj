package com.zq.ability.scan

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Vibrator
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.hmsscankit.OnResultCallback
import com.huawei.hms.hmsscankit.RemoteView
import com.huawei.hms.hmsscankit.ScanUtil
import com.huawei.hms.ml.scan.HmsScan
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions
import com.zq.ability.R
import com.zq.hilibrary.log.YLog

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-11-15 14:13
 **/
internal class ScanActivity : AppCompatActivity() {
    private var remoteView: RemoteView? = null

    private val flushBtn by lazy {
        findViewById<ImageView>(R.id.flush_btn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ability_activity_scan)
        // 绑定相机预览布局。
        val frameLayout = findViewById<FrameLayout>(R.id.container)
        // 设置扫码识别区域，您可以按照需求调整参数。
        val dm = getResources().getDisplayMetrics()
        val density = dm.density
        val mScreenWidth = getResources().getDisplayMetrics().widthPixels
        val mScreenHeight = getResources().getDisplayMetrics().heightPixels
        // 当前Demo扫码框的宽高是300dp。
        val SCAN_FRAME_SIZE = 240
        val scanFrameSize = (SCAN_FRAME_SIZE * density).toInt()
        val rect = Rect()
        rect.left = (mScreenWidth / 2 - scanFrameSize / 2).toInt()
        rect.right = (mScreenWidth / 2 + scanFrameSize / 2).toInt()
        rect.top = (mScreenHeight / 2 - scanFrameSize / 2).toInt()
        rect.bottom = (mScreenHeight / 2 + scanFrameSize / 2).toInt()
        // 初始化RemoteView，并通过如下方法设置参数:setContext()（必选）传入context、setBoundingBox()设置扫描区域、setFormat()设置识别码制式，设置完毕调用build()方法完成创建。通过setContinuouslyScan（可选）方法设置非连续扫码模式。
        remoteView =
            RemoteView.Builder().setContext(this).setBoundingBox(rect).setContinuouslyScan(false)
                .setFormat(HmsScan.ALL_SCAN_TYPE).build()
        // 将自定义view加载到activity的frameLayout中。
        remoteView?.onCreate(savedInstanceState)
        val params = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        frameLayout.addView(remoteView, params)

        remoteView?.setOnResultCallback(OnResultCallback {
            // 获取到扫码结果HmsScan

        })

        remoteView?.setOnLightVisibleCallback { visible ->
            flushBtn.visibility = if (visible) View.VISIBLE else View.GONE
        }

        initOperations()
    }

    private fun initOperations() {
        findViewById<ImageView>(R.id.back_img).setOnClickListener { finish() }
        flushBtn.setOnClickListener {
            if (remoteView?.lightStatus == true) {
                remoteView?.switchLight()
                flushBtn.setImageResource(R.drawable.scan_flashlight_off)
            } else {
                remoteView?.switchLight()
                flushBtn.setImageResource(R.drawable.scan_flashlight_on)
            }
        }

        findViewById<ImageView>(R.id.select_photo).setOnClickListener {
            val pickIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(pickIntent, REQUEST_CODE_SELECT_PHOTO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_SELECT_PHOTO && data != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
            val results = ScanUtil.decodeWithBitmap(
                this,
                bitmap,
                HmsScanAnalyzerOptions.Creator().setPhotoMode(true).create()
            )
            if (results != null && results.isNotEmpty() && results[0] != null && !TextUtils.isEmpty(
                    results[0].originalValue
                )
            ) {
                showResult(results[0])
            }
        }
    }

    private fun showResult(hmsScan: HmsScan) {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(300)

        ScanResult.onScanResult(hmsScan)

        YLog.e("scan_type:" + hmsScan.scanType + ",scan_reuslt：" + hmsScan.originalValue)
        finish()
    }

    override
    fun onStart() {
        super.onStart()
        // 侦听activity的onStart
        remoteView?.onStart()
    }

    override
    fun onResume() {
        super.onResume()
        // 侦听activity的onResume
        remoteView?.onResume()
    }

    override
    fun onPause() {
        super.onPause()
        // 侦听activity的onPause
        remoteView?.onPause()
    }

    override
    fun onStop() {
        super.onStop()
        // 侦听activity的onStop
        remoteView?.onStop()
    }

    override
    fun onDestroy() {
        super.onDestroy()
        // 侦听activity的onDestroy
        remoteView?.onDestroy()
    }

    companion object {
        const val REQUEST_CODE_SELECT_PHOTO = 100
    }
}