package com.dingyi.bilibilirank.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Build
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


private val numberFormat = NumberFormat.getNumberInstance().apply {
    maximumFractionDigits = 2
}

fun Int.formatNumber(): String {

    return when (this) {
        in 1000..9999 -> "${numberFormat.format(this.toFloat() / 1000.toFloat())}k"
        in 10000..Int.MAX_VALUE -> "${numberFormat.format(this.toFloat() / 10000.toFloat())}w"
        else -> this.toString()

    }

}

class Result<T>(private val isNullable: Boolean = false, private val self: T?) {
    fun nullable(block: () -> Unit): Result<T> {
        if (isNullable)
            block.invoke()
        return this
    }

    fun notNullable(block: (T) -> Unit): Result<T> {
        if (!isNullable)
            self?.apply(block)
        return this
    }

}

fun <T> checkIsNullable(self: T?, block: (T) -> Unit = {}): Result<T> {
    return Result(self == null, self).notNullable(block)
}

fun Context.getAttributeColor(resId: Int): Int {
    val typedArray = obtainStyledAttributes(intArrayOf(resId))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}


inline val openBrowserUrl: Activity.(String) -> Unit
    get() = { url ->

        val targetIntent = Intent(ACTION_VIEW, Uri.parse(url))
        targetIntent.apply {
            // 非浏览器应用会直接处理该 URL（默认情况下）
            // 用户也可以在消除歧义对话框中选择非浏览器应用
            addCategory(CATEGORY_BROWSABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_REQUIRE_NON_BROWSER
            }
        }.runCatching {
            startActivity(this)
        }.onFailure {
            startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
        }
    }

