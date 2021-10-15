package com.zq.asproj.model

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zq.hilibrary.util.DisplayUtil
import com.zq.ui.banner.Banner
import com.zq.ui.banner.core.BannerMo
import com.zq.ui.recycler.RVViewHolder
import com.zq.ui.recycler.RVDataItem


class BannerItem(val list: List<HomeBanner>) :
    RVDataItem<List<HomeBanner>, RVViewHolder>(list) {

    override fun onBindData(holder: RVViewHolder, position: Int) {
        val context = holder.itemView.context
        val banner = holder.itemView as Banner

        val models = mutableListOf<BannerMo>()
        list.forEachIndexed { index, homeBanner ->
            val bannerMo = object : BannerMo() {}
            bannerMo.url = homeBanner.cover
            models.add(bannerMo)
        }
        banner.setOnBannerClickListener { viewHolder, bannerMo, position ->
            val homeBanner = list[position]
            if (TextUtils.equals(homeBanner.type, HomeBanner.TYPE_GOODS)) {
                //跳详情页....
                Toast.makeText(context, "you  touch me:$position", Toast.LENGTH_SHORT).show()
            } else {
                // HiRoute.startActivity4Browser(homeBanner.url)
            }

        }
        banner.setBannerData(models)
        banner.setBindAdapter { viewHolder, mo, position ->
            ((viewHolder.rootView) as ImageView).load(mo.url)
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val context = parent.context
        val banner = Banner(context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            DisplayUtil.dp2px(160f)
        )
        params.bottomMargin = DisplayUtil.dp2px(10f)
        banner.layoutParams = params
        banner.setBackgroundColor(Color.WHITE)
        return banner
    }

}