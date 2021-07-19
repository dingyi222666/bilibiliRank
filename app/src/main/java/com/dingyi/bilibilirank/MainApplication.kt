package com.dingyi.bilibilirank

import android.app.Application
import kotlin.properties.Delegates

/**
 * @author: dingyi
 * @date: 2021/7/19 2:58
 * @description:
 **/
class MainApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        application=this
    }

    companion object {
        var application by Delegates.notNull<MainApplication>()
    }

}