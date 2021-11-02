package com.zq.asproj.fragment

import com.zq.common.flutter.FlutterCacheManager
import com.zq.common.flutter.FlutterFragment

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-16 15:11
 **/
class FavoriteFragment : FlutterFragment() {
    override val moduleName: String
        get() = FlutterCacheManager.MODULE_NAME_FAVORITE
//    private var urls = arrayOf(
//        "https://www.devio.org/img/beauty_camera/beauty_camera1.jpg",
//        "https://www.devio.org/img/beauty_camera/beauty_camera3.jpg",
//        "https://www.devio.org/img/beauty_camera/beauty_camera4.jpg",
//        "https://www.devio.org/img/beauty_camera/beauty_camera5.jpg",
//        "https://www.devio.org/img/beauty_camera/beauty_camera2.jpg",
//        "https://www.devio.org/img/beauty_camera/beauty_camera6.jpg",
//        "https://www.devio.org/img/beauty_camera/beauty_camera7.jpg",
//        "https://www.devio.org/img/beauty_camera/beauty_camera8.jpeg"
//    )
//
//    override fun getLayoutId(): Int {
//        return R.layout.activity_hi_banner_demo
//    }
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        initView()
//
//    }
//
//    private fun initView() {
//        var indicator: Indicator<*> = CircleIndicator(context!!)
//        val mBanner = layoutView.findViewById<Banner>(R.id.banner)
//        val moList: MutableList<BannerMo> = ArrayList()
//        for (i in 0..5) {
//            val mo = BannerMo()
//            mo.url = urls[i % urls.size]
//            moList.add(mo)
//        }
//        mBanner!!.setIndicator(indicator)
//        mBanner.setAutoPlay(true)
//        mBanner.setIntervalTime(2000)
//        //自定义布局
//        mBanner.setBannerData(moList)
//        mBanner.setBindAdapter { viewHolder, mo, position ->
//            val imageView: ImageView = viewHolder.findViewById(R.id.iv_image)
//            imageView.load(mo.url)
//
//        }
//        layoutView.findViewById<Switch>(R.id.auto_play).setOnCheckedChangeListener { _, isChecked ->
//            mBanner.setAutoPlay(isChecked)
//        }
//        layoutView.findViewById<View>(R.id.tv_switch).setOnClickListener {
//            if (indicator is CircleIndicator) {
//                indicator = NumberIndicator(context!!)
//            }else{
//                indicator = CircleIndicator(context!!)
//            }
//            mBanner!!.setIndicator(indicator)
//
//
//        }
//
//    }
}
