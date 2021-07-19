package com.dingyi.bilibilirank.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.dingyi.bilibilirank.bean.Info
import com.dingyi.bilibilirank.databinding.ItemRankListBinding
import com.dingyi.bilibilirank.ui.viewmodel.MainViewModel
import com.dingyi.bilibilirank.util.checkIsNullable
import com.dingyi.bilibilirank.util.dp

/**
 * @author: dingyi
 * @date: 2021/7/19 6:41
 * @description:
 **/
class RankListAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: MainViewModel,
    private val partition: String
) :
    RecyclerView.Adapter<RankListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRankListBinding) :
        RecyclerView.ViewHolder(binding.root) {}


    private var oldItem = listOf<Info>()

    private var newItem = listOf<Info>()

    init {
        viewModel.getRankList(partition).apply {
            observe(lifecycleOwner) {
                oldItem = newItem
                newItem = it
                notifyDataChanged()
            }
            checkIsNullable(this.value) {
                newItem = it
            }
        }
        notifyDataChanged()
    }


    private inner class RefreshCallBack : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition]=== newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition].bvid == newItem[newItemPosition].bvid
        }

    }

    lateinit var onClickListener: (Info) -> Unit

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
        viewModel.getRankList(partition).value?.let {
            val info = it[position]
            holder.binding.apply {
                this.info = info
                root.setOnClickListener {
                    onClickListener(info)
                }
            }

            Glide.with(holder.binding.image)
                .load(info.pic)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(6.dp)))
                .transition(DrawableTransitionOptions.withCrossFade(160))
                .into(holder.binding.image)


        }

    }

    override fun getItemCount(): Int {
        return viewModel.getRankList(partition).value?.size ?: 0
    }

    private fun notifyDataChanged() {
        DiffUtil.calculateDiff(RefreshCallBack(),false).dispatchUpdatesTo(this)
    }


}