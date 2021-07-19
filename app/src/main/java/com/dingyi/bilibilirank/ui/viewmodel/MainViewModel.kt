package com.dingyi.bilibilirank.ui.viewmodel

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dingyi.bilibilirank.bean.Info
import com.dingyi.bilibilirank.repository.RankRepository
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
        mutableMapOf<String, List<Info>>()
    }

    private val loadStateList = mutableMapOf<String, MutableLiveData<Int>>()


    fun setLoadState(type: String, flags: Boolean): LiveData<Int> {
        val base = if (flags) View.GONE else View.VISIBLE
        val data = loadStateList[type] ?: MutableLiveData(base)
        data.value = base
        loadStateList[type] = data
        return data

    }


    fun getLoadState(type: String): LiveData<Int> {
        return loadStateList[type] ?: setLoadState(type, false)!!
    }


    fun getRankList(type: String): List<Info>? {
        return rankList[type]
    }


    fun requestRank(partitionName: String, block: () -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            RankRepository
                .getVideoRank(partitionName)
                .flowOn(Dispatchers.IO)
                .onStart {
                    setLoadState(partitionName, false)
                }.catch { error ->
                    Log.e("errors", error.stackTraceToString())
                    setLoadState(partitionName, true)
                }.onCompletion {
                    setLoadState(partitionName, true)
                    block()
                }.collect { data ->
                    rankList[partitionName] = data.rank.list
                }
        }

    }

}