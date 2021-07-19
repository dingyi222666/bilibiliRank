package com.dingyi.bilibilirank.bean



data class RankJsonBean(
    val rank: Rank
) {
    data class Rank(
        val code: Int, // 0
        val list: List<Info>,
        val note: String, // 根据稿件内容质量、近期的数据综合展示，动态更新
        val num: Int, // 100
        val pages: Int // 1
    )

}