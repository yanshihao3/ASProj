package com.zq.asproj.fragment.detail

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.zq.asproj.R
import com.zq.asproj.model.DetailModel
import com.zq.hilibrary.util.DisplayUtil
import com.zq.ui.recycler.RVDataItem
import com.zq.ui.recycler.RVViewHolder
import kotlin.math.min

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-08 14:54
 **/
class CommentItem(val model: DetailModel) : RVDataItem<DetailModel, RVViewHolder>() {
    private lateinit var comment_title: TextView

    override fun onBindData(holder: RVViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val chipGroup = holder.findViewById<ChipGroup>(R.id.chip_group)
        comment_title = holder.findViewById<TextView>(R.id.comment_title)!!
        comment_title.text = model.commentCountTitle
        val commentTag: String? = model.commentTags
        if (commentTag != null) {
            val tagsArray: List<String>? = commentTag.split(" ")
            if (tagsArray != null && tagsArray.isNotEmpty()) {
                for (index in tagsArray.indices) {
                    //肯定会存在滑动复用的问题 ，此时创建标签的时候，我们需要检查有没有可以复用的
                    chipGroup?.visibility = View.VISIBLE
                    val chipLabel = if (index < chipGroup!!.childCount) {
                        chipGroup?.getChildAt(index) as Chip
                    } else {
                        val chipLabel = Chip(context)
                        chipLabel.chipCornerRadius = DisplayUtil.dp2px(4f).toFloat()
                        chipLabel.chipBackgroundColor =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.color_faf0
                                )
                            )
                        chipLabel.setTextColor(ContextCompat.getColor(context, R.color.color_999))
                        chipLabel.textSize = 14f
                        chipLabel.gravity = Gravity.CENTER
                        chipLabel.isCheckedIconVisible = false
                        chipLabel.isCheckable = false
                        chipLabel.isChipIconVisible = false

                        chipGroup?.addView(chipLabel)
                        chipLabel
                    }
                    chipLabel.text = tagsArray[index]
                }
            }
        }

        model.commentModels?.let {
            val commentContainer = holder.findViewById<LinearLayout>(R.id.comment_container)
            commentContainer?.visibility = View.VISIBLE
            for (index in 0..min(it.size - 1, 3)) {
                val comment = it[index]
                val itemView = if (index < commentContainer!!.childCount) {
                    commentContainer?.getChildAt(index)
                } else {
                    val view = LayoutInflater.from(context)
                        .inflate(R.layout.layout_detail_item_comment_item, commentContainer, false)
                    commentContainer?.addView(view)
                    view
                }
                val avatar = itemView.findViewById<ImageView>(R.id.user_avatar)
                avatar.load(comment.avatar) {
                    transformations(CircleCropTransformation())
                }
                itemView.findViewById<TextView>(R.id.user_name).text = comment.nickName
                itemView.findViewById<TextView>(R.id.comment_content).text = comment.content
            }
        }
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_comment
    }
}