package com.dingyi.bilibilirank.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dingyi.bilibilirank.databinding.ItemRankListBinding
import com.dingyi.bilibilirank.ui.viewmodel.MainViewModel
import com.dingyi.bilibilirank.util.dp

/**
 * @author: dingyi
 * @date: 2021/7/19 6:41
 * @description:
 **/
class RankListAdapter(private val viewModel: MainViewModel, private val partition: String) :
    RecyclerView.Adapter<RankListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRankListBinding) :
        RecyclerView.ViewHolder(binding.root) {}


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRankListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        viewModel.getRankList(partition)?.let {
            val info = it[position]
            holder.binding.info = info

            Glide.with(holder.binding.image)
                .load(info?.pic)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(6.dp)))
                .transition(DrawableTransitionOptions.withCrossFade(160))
                .into(holder.binding.image)

        }

    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

    }


    override fun getItemCount(): Int {
        return viewModel.getRankList(partition)?.size ?: 0
    }


}