package com.zq.common.flutter

/**
 * @program: ASProj
 *
 * @description:
 *
 * @author: 闫世豪
 *
 * @create: 2021-10-18 18:31
 **/
interface IBridge<P, Callback> {
    fun onBack(p: P?)
    fun goToNative(p: P)

    fun getHeaderParams(callback: Callback)
}