package com.zq.debugtool


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Debug(val name: String, val desc: String)
