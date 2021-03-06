package com.dingyi.bilibilirank.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.dingyi.bilibilirank.R
import com.dingyi.bilibilirank.bean.Info
import com.dingyi.bilibilirank.databinding.FragmentRankBinding
import com.dingyi.bilibilirank.ui.adapter.RankListAdapter
import com.dingyi.bilibilirank.ui.viewmodel.MainViewModel
import com.dingyi.bilibilirank.util.formatNumber
import com.dingyi.bilibilirank.util.getAttributeColor
import com.dingyi.bilibilirank.util.openBrowserUrl
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
        RankListAdapter(viewLifecycleOwner, viewModel, partitionName)
    }

    private var partitionName = ""

    inner class VisibilityData(val data: LiveData<Boolean>)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return FragmentRankBinding.inflate(inflater, container, false)
            .apply {
                binding = this
            }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun requestRankData() {
        viewModel.requestRank(partitionName)
    }

    override fun onResume() {
        super.onResume()

        if (viewModel.getRankList(partitionName).value?.isEmpty() == true) {
            requestRankData()
        }

    }

    private fun initView() {
        val observable = viewModel.getLoadState(partitionName)

        binding.apply {
            data = VisibilityData(observable)
            lifecycleOwner = viewLifecycleOwner
            list.adapter = adapter
            refresh.let {
                it.setColorSchemeColors(
                    requireContext().getAttributeColor(R.attr.colorPrimary)
                )
                it.setOnRefreshListener {
                    if (viewModel.getLoadState(partitionName).value == false) {
                        requestRankData()
                    }
                }
            }
            list.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        adapter.onClickListener = ::showInfoDialog

    }

    private fun showInfoDialog(it: Info) {
        AlertDialog.Builder(requireActivity())
            .apply {
                setTitle(it.title)
                setMessage(
                    """
                        ????????????:${it.title}
                        up???:${it.author}
                        ?????????:${it.video_review.formatNumber()}
                        ????????????:${it.pts}
                        ??????:${it.duration}
                        bv???:${it.bvid}
                    """.trimIndent()
                )
                setPositiveButton("?????????????????????") { _, _ ->
                    requireActivity().openBrowserUrl("https://bilibili.com/video/${it.bvid}")
                }
            }.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        partitionName = requireArguments().getString("partitionName").toString()


    }


}