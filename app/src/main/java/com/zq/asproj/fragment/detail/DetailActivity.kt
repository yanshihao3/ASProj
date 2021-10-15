package com.zq.asproj.fragment.detail

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zq.asproj.R
import com.zq.asproj.databinding.ActivityDetailBinding
import com.zq.asproj.model.DetailModel
import com.zq.asproj.model.GoodsItem
import com.zq.asproj.model.GoodsModel
import com.zq.asproj.model.selectPrice
import com.zq.common.ui.component.BaseActivity
import com.zq.common.ui.view.EmptyView
import com.zq.ui.recycler.RVAdapter
import com.zq.ui.recycler.RVDataItem


@Route(path = "/detail/main")
class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsModel: GoodsModel? = null

    private var emptyView: EmptyView? = null

    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        //关键代码
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val rootView: View = binding.root
        setContentView(rootView)
        initView()
        preBindData()
        queryDetailData()

    }

    private fun queryDetailData() {
        viewModel = DetailViewModel.get(goodsId, this)
        viewModel.queryDetailData().observe(this, Observer {
            if (it == null) {
                showEmptyView()
            } else {
                bindData(it)
            }
        })
    }

    private fun bindData(detailModel: DetailModel) {
        binding.recyclerView.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        val hiAdapter = binding.recyclerView.adapter as RVAdapter
        val dataItems = mutableListOf<RVDataItem<*, *>>()
        //头部模块
        dataItems.add(
            HeaderItem(
                detailModel.sliderImages,
                selectPrice(detailModel.groupPrice, detailModel.marketPrice),
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )

        //评论模块
        dataItems.add(
            CommentItem(detailModel)
        )
        //店铺模块
        dataItems.add(ShopItem(detailModel))

        //商品描述lauyou
        dataItems.add(GoodsAttrItem(detailModel))
        //图库
        detailModel.gallery?.forEach {
            dataItems.add(GalleryItem(it))
        }
        //相似商品
        detailModel.similarGoods?.let {
            dataItems.add(SimilarTitleItem())
            it.forEach {
                dataItems.add(GoodsItem(it, false))
            }
        }
        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)
    }

    private fun preBindData() {
        if (goodsModel == null) return
        val hiAdapter = binding.recyclerView.adapter as RVAdapter
        hiAdapter.addItemAt(
            0, HeaderItem(
                goodsModel!!.sliderImages,
                selectPrice(goodsModel!!.groupPrice, goodsModel!!.marketPrice),
                goodsModel!!.completedNumText,
                goodsModel!!.goodsName
            ), false
        )
    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this)
            emptyView!!.setIcon(R.string.if_empty3)
            emptyView!!.setDesc(getString(R.string.list_empty_desc))
            emptyView!!.layoutParams = ConstraintLayout.LayoutParams(-1, -1)
            emptyView!!.setBackgroundColor(Color.WHITE)
            emptyView!!.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                viewModel.queryDetailData()
            })

            binding.rootContainer.addView(emptyView)
        }

        binding.recyclerView.visibility = View.GONE
        emptyView!!.visibility = View.VISIBLE
    }

    private fun initView() {
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.recyclerView.adapter = RVAdapter(this)

    }
}