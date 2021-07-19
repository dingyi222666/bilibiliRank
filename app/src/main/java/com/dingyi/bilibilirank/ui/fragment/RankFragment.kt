package com.dingyi.bilibilirank.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.dingyi.bilibilirank.databinding.FragmentRankBinding
import com.dingyi.bilibilirank.ui.adapter.RankListAdapter
import com.dingyi.bilibilirank.ui.viewmodel.MainViewModel
import kotlin.properties.Delegates

/**
 * @author: dingyi
 * @date: 2021/7/19 3:43
 * @description:
 **/
class RankFragment() : Fragment() {

    private var binding by Delegates.notNull<FragmentRankBinding>()

    private val viewModel by activityViewModels<MainViewModel>()

    private val adapter by lazy {
        RankListAdapter(viewModel, partitionName)
    }

    private var partitionName = ""

    inner class VisibilityData(val data: LiveData<Int>)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val observable = viewModel.setLoadState(partitionName, false)

        return FragmentRankBinding.inflate(inflater, container, false)
            .apply {
                binding = this
                binding.data = VisibilityData(observable)
                lifecycleOwner = viewLifecycleOwner
                list.adapter = adapter
                list.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            }
            .root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        partitionName = requireArguments().getString("partitionName").toString()

        viewModel.requestRank(partitionName) {
            viewModel.getRankList(partitionName)?.let {
                adapter.notifyItemRangeInserted(
                    0,
                    it.size
                )
                adapter.notifyItemRangeChanged(0, it.size)
            }

        }
    }


}