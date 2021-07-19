package com.dingyi.bilibilirank.api

import com.dingyi.bilibilirank.bean.RankJsonBean
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author: dingyi
 * @date: 2021/7/19 4:34
 * @description:
 **/
interface RankApi {

    @GET("video_ranking")
    suspend fun getVideoRank(@Query("type") type:String,@Query("duration") duration:Int=3):RankJsonBean


}