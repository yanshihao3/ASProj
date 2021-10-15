package com.zq.asproj.biz.goods

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.zq.asproj.http.api.GoodsApi
import com.zq.asproj.model.GoodsItem
import com.zq.asproj.model.GoodsList
import com.zq.common.http.ApiFactory
import com.zq.common.ui.component.AbsListFragment
import com.zq.hilibrary.restful.HiCallback
import com.zq.hilibrary.restful.HiResponse

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-27 11:18
 **/
class GoodsListFragment : AbsListFragment() {
    @JvmField
    @Autowired
    var categoryId: String = ""

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ARouter.getInstance().inject(this)

        enableLoadMore { loadData() }
        loadData()
    }

    override fun onRefresh() {
        super.onRefresh()
        loadData()
    }

    private fun loadData() {
        ApiFactory.create(GoodsApi::class.java)
            .queryCategoryGoodsList(categoryId, subcategoryId, 10, pageIndex)
            .enqueue(object : HiCallback<GoodsList> {
                override fun onSuccess(response: HiResponse<GoodsList>) {
                    if (response.successful() && response.data != null) {
                        onQueryCategoryGoodsList(response.data!!)
                    } else {
                        finishRefresh(null)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    finishRefresh(null)
                }

            })
    }

    private fun onQueryCategoryGoodsList(data: GoodsList) {
        val dataItems = mutableListOf<GoodsItem>()
        for (goodsModel in data.list) {
            val goodsItem = GoodsItem(goodsModel, false)
            dataItems.add(goodsItem)
        }
        finishRefresh(dataItems)
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }
}