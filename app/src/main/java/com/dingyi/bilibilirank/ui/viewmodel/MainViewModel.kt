package com.dingyi.bilibilirank.ui.viewmodel

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dingyi.bilibilirank.MainApplication
import com.dingyi.bilibilirank.bean.Info
import com.dingyi.bilibilirank.repository.RankRepository
import com.dingyi.bilibilirank.util.checkIsNullable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.collections.set

/**
 * @author: dingyi
 * @date: 2021/7/19 2:57
 * @description:
 **/
class MainViewModel : ViewModel() {
    private val rankList by lazy {
        mutableMapOf<String, MutableLiveData<List<Info>>>()
    }

    private val loadStateList = mutableMapOf<String, MutableLiveData<Int>>()


    private fun setLoadState(type: String, flags: Boolean): LiveData<Int> {
        val base = if (flags) View.GONE else View.VISIBLE
        val data = loadStateList[type] ?: MutableLiveData(base)
        data.value = base
        loadStateList[type] = data
        return data

    }


    fun getLoadState(type: String): LiveData<Int> {
        return loadStateList[type] ?: setLoadState(type, false)
    }


    fun getRankList(type: String): LiveData<List<Info>> {
        return rankList[type] ?: MutableLiveData(listOf<Info>()).apply {
            rankList[type] = this
        }
    }


    fun requestRank(partitionName: String) {
        viewModelScope.launch(Dispatchers.Main) {
            RankRepository
                .queryVideoRank(partitionName)
                .flowOn(Dispatchers.IO)
                .onStart {
                    setLoadState(partitionName, false)
                }.catch { error ->
                    Log.e("errors", error.stackTraceToString())
                    Toast.makeText(
                        MainApplication.application,
                        "请求出错：$error",
                        Toast.LENGTH_LONG
                    ).show()
                    setLoadState(partitionName, false)
                }.onCompletion {
                    setLoadState(partitionName, true)

                }.collect { data ->
                    checkIsNullable(rankList[partitionName]) {
                        it.postValue(data.rank.list)
                    }.nullable {
                        rankList[partitionName] = MutableLiveData(data.rank.list)
                    }

                }
        }

    }

}