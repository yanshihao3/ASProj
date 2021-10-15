package com.zq.order

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import coil.load
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.zq.common.route.HiRoute
import com.zq.common.ui.component.BaseActivity
import com.zq.hilibrary.util.HiRes
import com.zq.hilibrary.util.HiStatusBar
import com.zq.order.address.AddEditingDialogFragment
import com.zq.order.address.Address
import com.zq.order.databinding.ActivityOrderBinding


@Route(path = "/order/main")
class OrderActivity : BaseActivity() {

    @JvmField
    @Autowired
    var shopName: String? = null

    @JvmField
    @Autowired
    var shopLogo: String? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsName: String? = null

    @JvmField
    @Autowired
    var goodsImage: String? = null

    @JvmField
    @Autowired
    var goodsPrice: String? = null

    private val REQUEST_CODE_ADDRESS_LIST = 1000

    private val viewModel by viewModels<OrderViewModel>()

    private lateinit var binding: ActivityOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, translucent = false)
        ARouter.getInstance().inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order)
        initView()
        updateTotalPayPrice(binding.amountView.getAmountValue())
        /*viewModel.queryMainAddress().observe(this) {
            updateAddress(it)
        }*/
        updateAddress(null)
    }

    private fun updateAddress(address: Address?) {
        val hasMainAddress = address != null && !TextUtils.isEmpty(address.receiver)
        binding.addAddress.visibility = if (hasMainAddress) View.GONE else View.VISIBLE
        binding.mainAddress.visibility = if (hasMainAddress) View.VISIBLE else View.GONE
        if (hasMainAddress) {
            binding.userName.text = address!!.receiver
            binding.userPhone.text = address.phoneNum
            binding.userAddress.text = "${address.province} ${address.city} ${address.area}"
            binding.mainAddress.setOnClickListener {
                //地址列表页
                HiRoute.startActivity(
                    this,
                    destination = "/address/list",
                    requestCode = REQUEST_CODE_ADDRESS_LIST
                )
            }
        } else {
            binding.addAddress.setOnClickListener {
                //弹窗 新增地址
                val addEditDialog = AddEditingDialogFragment.newInstance(null)
                addEditDialog.setSavedAddressListener(object :
                    AddEditingDialogFragment.onSavedAddressListener {
                    override fun onSavedAddress(address: Address) {
                        updateAddress(address)
                    }
                })
                addEditDialog.show(supportFragmentManager, "add_address")
            }
        }
    }

    private fun initView() {
        binding.navigationView.setNavListener {
            onBackPressed()
        }
        shopLogo?.let {
            binding.shopLogo.load(it)
        }
        binding.shopTitle.text = shopName
        goodsImage?.apply { binding.goodsImage.load(this) }
        binding.goodsTitle.text = goodsName
        binding.goodsPrice.text = goodsPrice
//计数器
        binding.amountView.setAmountValueChangedListener {
            updateTotalPayPrice(it)/*底部总价*/
        }
        //支付渠道
        binding.channelWxPay.setOnClickListener(channelPayListener)
        binding.channelAliPay.setOnClickListener(channelPayListener)

        //立即购买
        binding.orderNow.setOnClickListener {
            showToast("not support for now")
        }
    }

    private val channelPayListener = View.OnClickListener {
        val aliPayChecked = it.id == binding.channelAliPay.id
        binding.channelAliPay.isChecked = aliPayChecked
        binding.channelWxPay.isChecked = !aliPayChecked
    }

    private fun updateTotalPayPrice(amount: Int) {
        //amount* goodsPrice.contains("¥")
        // goodsPrice =10.08 ，x ￥
        binding.totalPayPrice.text = String.format(
            HiRes.getString(R.string.free_transport, PriceUtil.calculate(goodsPrice, amount))
        )
    }
}