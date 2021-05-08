package com.kohuyn.basemvvm.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import com.core.BaseFragment
import com.core.OnItemClick
import com.event.EventNextFragment
import com.kohuyn.basemvvm.R
import com.kohuyn.basemvvm.databinding.DialogYesNoBinding
import com.kohuyn.basemvvm.databinding.FragmentMainBinding
import com.kohuyn.basemvvm.databinding.LayoutLoadMoreBinding
import com.kohuyn.basemvvm.ui.main.adapter.UserAdapter
import com.kohuyn.basemvvm.ui.repo.RepositoriesUserFragment
import com.kohuyn.basemvvm.ui.utils.MarginItemDecoration
import com.utils.ext.clickWithDebounce
import com.utils.ext.postNormal
import com.widget.SwipeRecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by KOHuyn on 1/29/2021
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun getLayoutBinding(): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater)

    private val mainViewModel by viewModel<MainViewModel>()

    private val userAdapter by lazy { UserAdapter() }

    private var isLoadMore: Boolean = false
    private var isLoadingMore: Boolean = false

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.rcvUserGit.setUpRcv(userAdapter)
        binding.rcvUserGit.recyclerView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.space_4).toInt()
            )
        )
//        binding.rcvUserGit.addNoDataLayout(LayoutLoadMoreBinding.inflate(layoutInflater,binding.root,true))
        binding.rcvUserGit.addNoDataLayout(
            LayoutLoadMoreBinding.inflate(
                layoutInflater,
                binding.root,
                true
            )
        ) {
            it.btnReload.clickWithDebounce {
                mainViewModel.getAllUserSafe()
                binding.rcvUserGit.setNoDataVisibility(false)
            }
        }
//        binding.rcvUserGit.addNoDataLayout(R.layout.layout_load_more) {
//            it.findViewById<Button>(R.id.btnReload).clickWithDebounce {
//                mainViewModel.getAllUserSafe()
//            }
//        }
        binding.rcvUserGit.setOnSwipeLayoutListener({
            isLoadMore = false
            mainViewModel.getAllUserSafe()
            binding.rcvUserGit.setNoDataVisibility(false)
        }, {
            isLoadMore = true
            if (!isLoadingMore) {
                mainViewModel.getAllUserSafe()
                Log.e("loadMore: ", "OnLoadMore")
            }
        })

        callbackViewModel(mainViewModel)
        mainViewModel.getAllUserSafe()
        userAdapter.onItemClick = OnItemClick { item, _ ->
            val bundle = Bundle().apply {
                putString(RepositoriesUserFragment.ARG_NAME, item.login)
            }
            postNormal(EventNextFragment(RepositoriesUserFragment::class.java, bundle, true))
        }
    }

    private fun callbackViewModel(vm: MainViewModel) {
        addDispose(
            vm.rxUsers.subscribe {
                if (isLoadMore) {
                    userAdapter.items.addAll(it.toMutableList())
                } else {
                    userAdapter.items = it.toMutableList()
                }
            },
            vm.rxMessage.subscribe {
                binding.rcvUserGit.textNoData = it
                toast(it)
            },
            vm.isLoading.subscribe {
                if (isLoadMore) {
                    isLoadingMore = it
                    binding.rcvUserGit.setLoadMoreVisibility(it)
                } else {
                    binding.rcvUserGit.setRefresh(it)
                }
            },
            vm.rxNoDataCallback.subscribe {
                binding.rcvUserGit.setNoDataVisibility(it)
                userAdapter.items.clear()
            }
        )
    }
}