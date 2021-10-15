package com.zq.order.address

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.zq.common.city.CityMgr
import com.zq.common.ext.showToast
import com.zq.hilibrary.util.HiRes
import com.zq.order.R
import com.zq.order.databinding.DialogAddNewAddressBinding
import com.zq.ui.cityselector.CitySelectorDialogFragment
import com.zq.ui.cityselector.Province

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-13 15:17
 **/
class AddEditingDialogFragment : DialogFragment() {
    //对现有地址进行编辑
    private var address: Address? = null
    private var selectProvince: Province? = null
    private var binding: DialogAddNewAddressBinding? = null

    companion object {
        const val KEY_ADDRESS_PARAMS = "key_address"
        fun newInstance(address: Address?): AddEditingDialogFragment {
            val args = Bundle()
            args.putParcelable(KEY_ADDRESS_PARAMS, address)
            val fragment = AddEditingDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel by viewModels<AddressViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = arguments?.getParcelable<Address>(KEY_ADDRESS_PARAMS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        //使用android.R.id.content 作为 父容器，在inflate 布局的时候，
        // 会使用它的layoutParams 来设置子view的宽高
        val root = window?.findViewById(android.R.id.content) ?: container
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_add_new_address,
            root/*以window下的content为父布局*/,
            false
        )

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        return binding!!.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //右侧关闭按钮
        val closeBtn = binding!!.navNar.addRightTextButton(R.string.if_close)
        closeBtn.textSize = 25f
        closeBtn.setOnClickListener { dismiss() }

        //选择城市区域
        binding!!.addressPick.getEditText()
            .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_right_arrow, 0)
        //既不会拉起软键盘，还能够响应点击事件。不能用enable=false点击也没有了
        binding!!.addressPick.getEditText().isFocusable = false
        binding!!.addressPick.getEditText().isFocusableInTouchMode = false
        binding!!.addressPick.getEditText().setOnClickListener {

            //拉起城市选择器
            val liveData = CityMgr.getCityData()
            liveData.removeObservers(viewLifecycleOwner)/*防止多次触发*/
            liveData.observe(viewLifecycleOwner, Observer {
                //拉起城市选择器
                if (it != null) {
                    val citySelector = CitySelectorDialogFragment.newInstance(selectProvince, it)
                    citySelector.setCitySelectListener(object :
                        CitySelectorDialogFragment.onCitySelectListener {
                        override fun onCitySelect(province: Province) {
                            updateAddressPick(province)
                        }
                    })
                    citySelector.show(childFragmentManager/*子fragment*/, "city_selector")
                } else {
                    showToast(HiRes.getString(R.string.city_data_set_empty))
                }
            })
        }
        //详细地址
        binding!!.addressDetail.getTitleView().gravity = Gravity.TOP
        binding!!.addressDetail.getEditText().gravity = Gravity.TOP
        binding!!.addressDetail.getEditText().maxLines = 1


        //有address，数据回填
        if (address != null) {
            binding!!.userName.getEditText().setText(address!!.receiver)
            binding!!.userPhone.getEditText().setText(address!!.phoneNum)
            binding!!.addressPick.getEditText()
                .setText("${address!!.province} ${address!!.city} ${address!!.area}")
            binding!!.addressDetail.getEditText().setText(address!!.detail)
        }

        binding!!.actionSaveAddress.setOnClickListener {
            //保存地址
            savedAddress()
        }
    }

    private fun updateAddressPick(province: Province) {
        this.selectProvince = province
        binding!!.addressPick.getEditText()
            .setText("${province.districtName} ${province.selectCity?.districtName} ${province.selectDistrict?.districtName}")
    }

    private fun savedAddress() {
        val phone = binding!!.userPhone.getEditText().text.toString().trim()
        val receiver = binding!!.userName.getEditText().text.toString().trim()
        val detail = binding!!.addressDetail.getEditText().text.toString().trim()
        val cityArea = binding!!.addressPick.getEditText().text.toString().trim()
        if (TextUtils.isEmpty(phone)
            || TextUtils.isEmpty(receiver)
            || TextUtils.isEmpty(detail)
            || TextUtils.isEmpty(cityArea)
        ) {
            showToast(HiRes.getString(R.string.address_info_too_simple))
            return
        }
        val province = selectProvince?.districtName ?: address?.province
        val city = selectProvince?.selectCity?.districtName ?: address?.city
        val district = selectProvince?.selectDistrict?.districtName ?: address?.area
        if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city) || TextUtils.isEmpty(district)) {
            showToast(HiRes.getString(R.string.address_info_too_simple))
            return
        }
        if (address == null) {
            //新增地址保存
            viewModel.saveAddress(province!!, city!!, district!!, detail, receiver, phone)
                .observe(viewLifecycleOwner, observer)
        } else {
            //更新地址
            viewModel.updateAddress(
                address!!.id,
                province!!,
                city!!,
                district!!,
                detail,
                receiver,
                phone
            )
                .observe(viewLifecycleOwner, observer)
        }

    }

    private val observer = Observer<Address?> {
        if (it != null) {
            //回传结果
            savedAddressListener?.onSavedAddress(it)
            dismiss()
        }
    }
    private var savedAddressListener: onSavedAddressListener? = null

    fun setSavedAddressListener(listener: onSavedAddressListener) {
        this.savedAddressListener = listener
    }

    interface onSavedAddressListener {
        fun onSavedAddress(address: Address)
    }
}