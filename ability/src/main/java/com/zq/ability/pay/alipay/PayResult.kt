package com.zq.ability.pay.alipay

class PayResult constructor(map: Map<String, String?>) {
    val resultInfo = map["result"]//支付结果
    val resultStatus = map["resultStatus"]//支付结果状态码
    val memo = map["memo"]//错误信息提示信息

}