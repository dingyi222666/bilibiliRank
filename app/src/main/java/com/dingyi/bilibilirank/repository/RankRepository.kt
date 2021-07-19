package com.dingyi.bilibilirank.repository

import com.dingyi.bilibilirank.api.RankApi
import com.dingyi.bilibilirank.bean.RankJsonBean
import com.dingyi.bilibilirank.data.partitionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author: dingyi
 * @date: 2021/7/19 4:38
 * @description:
 **/
object RankRepository {
    private const val BASE_URL = "https://hibiapi.lite0.com/api/bilibili/v3/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun queryVideoRank(partitionName: String): Flow<RankJsonBean> = flow {

        val rankApi = retrofit.create(RankApi::class.java)
        partitionData.find { it.first == partitionName }?.let {
            val data = rankApi.getVideoRank(it.second)
            emit(data)
        }

    }


}