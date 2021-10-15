package com.zq.asproj.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import coil.load
import com.zq.asproj.R
import com.zq.asproj.http.api.CategoryApi
import com.zq.asproj.model.Subcategory
import com.zq.asproj.model.TabCategory
import com.zq.common.http.ApiFactory
import com.zq.common.ui.component.BaseFragment
import com.zq.hilibrary.restful.HiCallback
import com.zq.hilibrary.restful.HiResponse
import com.zq.ui.slider.HiSliderView

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-16 15:11
 **/

class CategoryFragment : BaseFragment() {
    lateinit var sliderView: HiSliderView
    override fun getLayoutId(): Int {
        return R.layout.fragment_category
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sliderView = layoutView.findViewById(R.id.sliderView)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queryCategoryList()
    }

    private fun queryCategoryList() {
        ApiFactory.create(CategoryApi::class.java).queryCategoryList()
            .enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    response.data?.let {
                        onQueryCategoryList(it)
                    }

                }

                override fun onFailed(throwable: Throwable) {
                    showEmptyView()
                }

            })
    }

    private fun onQueryCategoryList(data: List<TabCategory>?) {
        if (!isAlive) return
        sliderView.bindMenuView(itemCount = data!!.size, onBindView = { holder, i ->
            holder.findViewById<TextView>(R.id.menu_item_title)?.text = data[i].categoryName

        }, onItemClick = { holder, i ->
            querySubcategoryList(data[i].categoryId)
        })
    }

    private fun querySubcategoryList(categoryId: String) {
        ApiFactory.create(CategoryApi::class.java).querySubcategoryList(categoryId)
            .enqueue(object : HiCallback<List<Subcategory>> {
                override fun onSuccess(response: HiResponse<List<Subcategory>>) {
                    onQuerySubcategoryList(response.data)
                }

                override fun onFailed(throwable: Throwable) {
                    showEmptyView()
                }
            })
    }

    private val layoutManager = GridLayoutManager(context, Companion.SPAN_COUNT)
    private val subcategoryList = mutableListOf<Subcategory>()
    private val groupSpanSizeOffset = SparseIntArray()

    private val spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            var spanSize = 1
            val groupName: String = subcategoryList[position].groupName
            val nextGroupName: String? =
                if (position + 1 < subcategoryList.size) subcategoryList[position + 1].groupName else null

            if (TextUtils.equals(groupName, nextGroupName)) {
                spanSize = 1
            } else {
                //当前位置和 下一个位置 不再同一个分组
                //1 .要拿到当前组 position （所在组）在 groupSpanSizeOffset 的索引下标
                //2 .拿到 当前组前面一组 存储的 spansizeoffset 偏移量
                //3 .给当前组最后一个item 分配 spansize count

                val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
                val size = groupSpanSizeOffset.size()
                val lastGroupOffset = if (size <= 0) 0
                else if (indexOfKey >= 0) {
                    //说明当前组的偏移量记录，已经存在了 groupSpanSizeOffset ，这个情况发生在上下滑动，
                    if (indexOfKey == 0) 0 else groupSpanSizeOffset.valueAt(indexOfKey - 1)
                } else {
                    //说明当前组的偏移量记录，还没有存在于 groupSpanSizeOffset ，这个情况发生在 第一次布局的时候
                    //得到前面所有组的偏移量之和
                    groupSpanSizeOffset.valueAt(size - 1)
                }
                //          3       -     (6     +    5               % 3  )第几列=0  ，1 ，2
                spanSize = SPAN_COUNT - (position + lastGroupOffset) % SPAN_COUNT
                if (indexOfKey < 0) {
                    //得到当前组 和前面所有组的spansize 偏移量之和
                    val groupOffset = lastGroupOffset + spanSize - 1
                    groupSpanSizeOffset.put(position, groupOffset)
                }
            }

            return spanSize
        }

    }

    private fun onQuerySubcategoryList(data: List<Subcategory>?) {
        if (!isAlive) return
        subcategoryList.clear()
        subcategoryList.addAll(data!!)
        sliderView.bindContentView(
            itemCount = data!!.size,
            layoutManager = layoutManager,
            itemDecoration = null,
            onBindView = { holder, i ->
                holder.findViewById<TextView>(R.id.content_item_title)?.text =
                    data[i].subcategoryName
                val image = holder.findViewById<ImageView>(R.id.content_item_image)
                image?.load(data[i].subcategoryIcon)
            },
            onItemClick = { holder, i ->
                querySubcategoryList(data[i].categoryId)
            })
    }

    private fun showEmptyView() {

    }

    companion object {
        private const val SPAN_COUNT = 3
    }
}