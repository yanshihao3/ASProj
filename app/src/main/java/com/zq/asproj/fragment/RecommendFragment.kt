package com.zq.asproj.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.zq.asproj.R
import com.zq.asproj.TinkerTest
import com.zq.common.ui.component.BaseFragment

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-09-16 15:11
 **/

class RecommendFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_recommend
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = layoutView.findViewById<TextView>(R.id.recommend)

        textView.setOnClickListener {
            Toast.makeText(context, TinkerTest.getText(), Toast.LENGTH_LONG).show()
        }
    }
}