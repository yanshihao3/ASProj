package com.zq.asproj.fragment.detail

import com.zq.asproj.R
import com.zq.ui.recycler.RVDataItem
import com.zq.ui.recycler.RVViewHolder

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-08 16:29
 **/
class SimilarTitleItem : RVDataItem<Any, RVViewHolder>() {
    override fun onBindData(holder: RVViewHolder, position: Int) {

    }
    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similar_title
    }
}