package com.zq.asproj.fragment.detail

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.zq.asproj.R
import com.zq.asproj.model.BannerItem
import com.zq.asproj.model.SliderImage
import com.zq.ui.banner.Banner
import com.zq.ui.banner.core.BannerAdapter
import com.zq.ui.banner.core.BannerMo
import com.zq.ui.banner.indicator.NumberIndicator
import com.zq.ui.recycler.RVDataItem
import com.zq.ui.recycler.RVViewHolder

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-08 14:26
 **/
class HeaderItem(
    val sliderImages: List<SliderImage>?,
    val price: String?,
    val completedNumText: String?,
    val goodsName: String?
) : RVDataItem<Any, RVViewHolder>() {

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_header
    }

    override fun onBindData(holder: RVViewHolder, position: Int) {
        val context = holder.itemView.context

        val bannerItems = arrayListOf<BannerMo>()
        sliderImages?.forEach {
            val bannerMo = object : BannerMo() {}
            bannerMo.url = it.url
            bannerItems.add(bannerMo)
        }
        val banner = holder.findViewById<Banner>(R.id.hi_banner)
        banner?.setIndicator(NumberIndicator(context))
        banner?.setBannerData(bannerItems)
        banner?.setBindAdapter { viewHolder: BannerAdapter.BannerViewHolder?, mo: BannerMo?, position: Int ->
            val imageView = viewHolder?.rootView as? ImageView
            mo?.let { imageView?.load(it.url) }
        }

        holder.findViewById<TextView>(R.id.price)?.text = spanPrice(price)
        holder.findViewById<TextView>(R.id.sale_desc)?.text = completedNumText
        holder.findViewById<TextView>(R.id.title)?.text = goodsName
    }

    private fun spanPrice(price: String?): CharSequence {
        if (TextUtils.isEmpty(price)) return ""

        val ss = SpannableString(price)
        ss.setSpan(AbsoluteSizeSpan(18, true), 1, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }
}