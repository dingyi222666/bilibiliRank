package com.dingyi.bilibilirank.bean

import com.dingyi.bilibilirank.util.formatNumber

/**
 * @author: dingyi
 * @date: 2021/7/19 6:29
 * @description:
 **/

data class Info(
    val aid: String, // 589248806
    val author: String, // 徐大虾咯
    val bvid: String, // BV1VB4y1K7eL
    val cid: Int, // 371029115
    val coins: Int, // 0
    val duration: String, // 10:12
    val mid: Int, // 13354765
    val pic: String, // https://i0.hdslb.com/bfs/archive/2096bab25f8838d2583af9643d669effccf139dc.jpg
    val play: Int, // 5263501
    val pts: Int, // 5983453
    val title: String, // 不要笑挑战
    val trend: Any?, // null
    val video_review: Int // 66474
) {

    val video_review_format: String
        get() = "弹幕：${video_review.formatNumber()}"

    val pts_format: String
        get() = "综合得分：${pts.formatNumber()}"

}