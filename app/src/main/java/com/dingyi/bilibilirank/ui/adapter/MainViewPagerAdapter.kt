package com.dingyi.bilibilirank.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dingyi.bilibilirank.ui.fragment.RankFragment
import com.dingyi.bilibilirank.util.newFragmentInstance

/**
 * @author: dingyi
 * @date: 2021/7/19 3:39
 * @description:
 **/
class MainViewPagerAdapter(private val activity: FragmentActivity) :
    FragmentStateAdapter(activity) {


    private val partitionList = mutableListOf<String>()

    override fun getItemCount(): Int {
        return partitionList.size
    }

    fun addPartitionList(list: List<String>) {
        partitionList.addAll(list)

        notifyDataSetChanged()
    }

    override fun createFragment(position: Int): Fragment {
        return newFragmentInstance<RankFragment>("partitionName" to partitionList[position])
    }
}