package com.dingyi.bilibilirank.util

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.dingyi.bilibilirank.MainApplication
import java.text.NumberFormat

/**
 * @author: dingyi
 * @date: 2021/7/19 9:33
 * @description:
 **/

inline val Int.dp: Int
    get() = (0.5f + MainApplication.application.resources.displayMetrics.density * this).toInt()

inline fun <reified T : Fragment>
        newFragmentInstance(vararg params: Pair<String, Any>) =
    T::class.java.newInstance().apply {
        arguments = bundleOf(*params)
    }


private val numberFormat=NumberFormat.getNumberInstance().apply {
    maximumFractionDigits=2
}

fun Int.formatNumber(): String {

    return when (this) {
        in 1000..9999 -> "${numberFormat.format(this.toFloat() / 1000.toFloat())}k"
        in 10000..Int.MAX_VALUE -> "${numberFormat.format(this.toFloat() / 10000.toFloat())}w"
        else -> this.toString()

    }

}