package com.zq.order.address

import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.zq.hilibrary.util.HiRes
import com.zq.order.R
import com.zq.ui.icfont.IconFontTextView
import com.zq.ui.recycler.RVDataItem
import com.zq.ui.recycler.RVViewHolder


class AddressItem(
    var address: Address,
    val fm: FragmentManager,
    val removeItemCallback: (Address, AddressItem) -> Unit,
    val itemClickCallback: (Address) -> Unit,
    val viewModel: AddressViewModel
) : RVDataItem<Address, RVViewHolder>() {


    override fun onBindData(holder: RVViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.findViewById<TextView>(R.id.user_name).text = address.receiver
        holder.findViewById<TextView>(R.id.user_phone).text = address.phoneNum
        holder.findViewById<TextView>(R.id.user_address).text =
            "${address.province} ${address.city} ${address.area}"
        holder.findViewById<TextView>(R.id.edit_address).setOnClickListener {
            val dialog = AddEditingDialogFragment.newInstance(address)
            dialog.setSavedAddressListener(object :
                AddEditingDialogFragment.onSavedAddressListener {
                override fun onSavedAddress(newAddress: Address) {
                    address = newAddress
                    refreshItem()
                }
            })
            dialog.show(fm, "edit_address")
        }

        holder.findViewById<IconFontTextView>(R.id.delete).setOnClickListener {
            AlertDialog.Builder(context).setMessage(HiRes.getString(R.string.address_delete_title))
                .setNegativeButton(R.string.address_delete_cancel, null)
                .setPositiveButton(R.string.address_delete_ensure) { dialog, which ->
                    dialog.dismiss()
                    removeItemCallback(address, this)
                }.show()
        }
        holder.itemView.setOnClickListener {
            itemClickCallback(address)
        }

        holder.findViewById<TextView>(R.id.default_address).setOnClickListener {
            viewModel.checkedPosition = position
            viewModel.checkedAddressItem?.refreshItem()
            viewModel.checkedAddressItem = this
            refreshItem()
        }
        /*选中项样式*/
        val select = viewModel.checkedPosition == position && viewModel.checkedAddressItem == this
        holder.findViewById<TextView>(R.id.default_address)
            .setTextColor(HiRes.getColor(if (select) R.color.color_dd2 else R.color.color_999))
        holder.findViewById<TextView>(R.id.default_address).text =
            HiRes.getString(if (select) R.string.address_default else R.string.set_default_address)
        holder.findViewById<TextView>(R.id.default_address).setCompoundDrawablesWithIntrinsicBounds(
            if (select) R.drawable.ic_checked_red else 0,
            0,
            0, 0
        )
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.activity_address_list_item
    }
}