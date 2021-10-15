package com.zq.order.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.zq.common.route.RouteFlag
import com.zq.common.ui.component.BaseActivity
import com.zq.common.ui.view.EmptyView
import com.zq.order.R
import com.zq.ui.recycler.RVAdapter
import com.zq.ui.title.NavigationBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
@Route(path = "/address/list", extras = RouteFlag.FLAG_LOGIN)
class AddressListActivity : BaseActivity() {

    private val recyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycler_view)
    }

    private val navBar: NavigationBar by lazy {
        findViewById(R.id.nav_bar)
    }

    private val root_layout by lazy {
        findViewById<LinearLayout>(R.id.root_layout)
    }

    @Inject
    lateinit var emptyView: EmptyView
    private val viewModel: AddressViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)
        initView()
        viewModel.queryAddressList().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                bindData(it)
            } else {
                showHideEmptyView(true)
            }
        })
    }

    private fun bindData(list: List<Address>) {
        val items = arrayListOf<AddressItem>()
        for (address in list) {
            items.add(newAddressItem(address))
        }
        val hiAdapter = recyclerView.adapter as RVAdapter
        hiAdapter.clearItems()
        hiAdapter.addItems(items, true)
    }

    private fun newAddressItem(address: Address): AddressItem {
        return AddressItem(
            address,
            supportFragmentManager/*弹确认框用*/,
            itemClickCallback = { address ->
                val intent = Intent()
                intent.putExtra("result", address)
                setResult(Activity.RESULT_OK, intent)
                finish()
            },
            removeItemCallback = { address, addressItem ->
                viewModel.deleteAddress(addressId = address.id).observe(this@AddressListActivity,
                    Observer { success ->
                        if (success) {
                            addressItem.removeItem()
                        }
                    })
            },
            viewModel = viewModel
        )
    }

    private fun initView() {
        navBar.setNavListener {
            onBackPressed()
        }
        navBar.addRightTextButton(R.string.add_order_address)
            .setOnClickListener {


            }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RVAdapter(this)
        recyclerView.adapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    private fun showHideEmptyView(showEmptyView: Boolean) {
        recyclerView.isVisible = !showEmptyView
        emptyView.isVisible = showEmptyView /*isVisible是setVisibility的扩展函数*/
        if (emptyView.parent == null && showEmptyView) {
            root_layout.addView(emptyView)
        }
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            showHideEmptyView(false)
        }


        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            recyclerView.post {/*post一下才准确*/
                if (recyclerView.adapter!!.itemCount <= 0) {
                    showHideEmptyView(true)
                }
            }
        }
    }

    override fun onDestroy() {/*反注册*/
        recyclerView?.adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        super.onDestroy()
    }
}