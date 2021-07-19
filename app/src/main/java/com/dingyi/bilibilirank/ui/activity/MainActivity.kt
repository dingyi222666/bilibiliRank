package com.dingyi.bilibilirank.ui.activity

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dingyi.bilibilirank.R
import com.dingyi.bilibilirank.data.partitionData
import com.dingyi.bilibilirank.databinding.ActivityMainBinding
import com.dingyi.bilibilirank.ui.adapter.MainViewPagerAdapter
import com.dingyi.bilibilirank.ui.viewmodel.MainViewModel
import com.dingyi.bilibilirank.util.getAttributeColor
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private val viewModel: MainViewModel by viewModels()

    private val mainPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()

    }

    private fun initView() {

        binding.apply {
            pager.adapter = mainPagerAdapter

            partitionData.forEach {
                tab.addTab(tab.newTab().setText(it.first))
            }
            TabLayoutMediator(
                binding.tab,
                binding.pager,
                true
            ) { tab, position ->
                tab.text = partitionData[position].first
            }.attach()



        }


        mainPagerAdapter.apply {
            addPartitionList(partitionData.map { it.first })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("关于")
            .setMessage(Html.fromHtml(getString(R.string.about_html)))
            .setPositiveButton(android.R.string.ok, null)
            .show()
            .findViewById<TextView>(android.R.id.message)?.apply {
                movementMethod = LinkMovementMethod.getInstance();
                isClickable = true;
            }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.about -> showAboutDialog()
        }
        return super.onOptionsItemSelected(item)
    }

}