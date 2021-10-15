package com.zq.asproj.fragment.detail

import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.zq.asproj.R
import com.zq.asproj.model.DetailModel
import com.zq.common.ui.view.InputItemLayout
import com.zq.ui.recycler.RVDataItem
import com.zq.ui.recycler.RVViewHolder

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-08 15:50
 **/
class GoodsAttrItem(val detailModel: DetailModel) : RVDataItem<DetailModel, RVViewHolder>() {

    override fun onBindData(holder: RVViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val goodAttr = detailModel.goodAttr
        goodAttr?.let {
            val iterator = it.iterator()
            var index = 0
            val attrContainer = holder.findViewById<LinearLayout>(R.id.attr_container)
            attrContainer?.visibility = View.VISIBLE
            while (iterator.hasNext()) {
                val attr = iterator.next()
                val entries = attr.entries
                val key = entries.first().key
                val value = entries.first().value

                val attrItemView: InputItemLayout = if (index < attrContainer!!.childCount) {
                    attrContainer.getChildAt(index)
                } else {
                    LayoutInflater.from(context)
                        .inflate(
                            R.layout.layout_detail_item_attr_item,
                            attrContainer,
                            false
                        )
                } as InputItemLayout

                attrItemView.getEditText().hint = value
                attrItemView.getEditText().isEnabled = false
                attrItemView.getTitleView().text = key

                if (attrItemView.parent == null) {
                    attrContainer.addView(attrItemView)
                }
                index++
            }
        }

        detailModel.goodDescription?.let {
            val attrDesc = holder.findViewById<TextView>(R.id.attr_desc)
            attrDesc?.visibility = View.VISIBLE
            attrDesc?.text = it
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_attr
    }
}