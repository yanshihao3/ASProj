package com.zq.asproj.fragment.detail

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zq.asproj.model.SliderImage
import com.zq.ui.recycler.RVDataItem
import com.zq.ui.recycler.RVViewHolder

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-08 15:59
 **/
class GalleryItem(val sliderImage: SliderImage) : RVDataItem<SliderImage, RVViewHolder>() {

    private var parentWidth: Int = 0

    override fun onBindData(holder: RVViewHolder, position: Int) {
        val imageView = holder.itemView as ImageView
        if (!TextUtils.isEmpty(sliderImage.url)) {
            imageView.load(sliderImage.url) {
                listener(onSuccess = { request, _ ->
                    val drawable = request.fallback
                    val drawableWidth = drawable!!.intrinsicWidth
                    val drawableHeight = drawable!!.intrinsicHeight

                    val params = imageView.layoutParams ?: RecyclerView.LayoutParams(
                        parentWidth,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                    params.width = parentWidth
                    params.height = (drawableHeight / (drawableWidth * 1.0f / parentWidth)).toInt()
                    imageView.layoutParams = params
                    ViewCompat.setBackground(imageView, drawable)
                })

            }
            //展位图
            //需要拿到图片加载后的回到，
            // 根据图片的宽高的值，等比计算imageview高度值

        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val imageView = ImageView(parent.context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setBackgroundColor(Color.WHITE)
        return imageView
    }

    override fun onViewAttachedToWindow(holder: RVViewHolder) {
        //提前给imageview 预设一个高度值  等于parent的宽度
        parentWidth = (holder.itemView.parent as ViewGroup).measuredWidth
        val params = holder.itemView.layoutParams
        if (params.width != parentWidth) {
            params.width = parentWidth
            params.height = parentWidth
            holder.itemView.layoutParams = params
        }
    }

}