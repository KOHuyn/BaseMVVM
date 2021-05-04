package com.kohuyn.basemvvm.ui.main

import android.os.Bundle
import com.core.BaseFragment
import com.core.OnItemClick
import com.event.EventNextFragment
import com.kohuyn.basemvvm.R
import com.kohuyn.basemvvm.databinding.FragmentMainBinding
import com.kohuyn.basemvvm.ui.dialog.YesNoDialog
import com.kohuyn.basemvvm.ui.main.adapter.UserAdapter
import com.kohuyn.basemvvm.ui.repo.RepositoriesUserFragment
import com.kohuyn.basemvvm.ui.utils.MarginItemDecoration
import com.utils.ext.clickWithDebounce
import com.utils.ext.postNormal
import com.widget.SwipeRecyclerView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
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

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.rcvUserGit.setUpRcv(userAdapter)
        binding.rcvUserGit.rcvCore.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.space_4).toInt()
            )
        )
        binding.rcvUserGit.onCustomSwipeListener = SwipeRecyclerView.OnCustomSwipeListener {
            mainViewModel.getAllUser()
        }
        callbackViewModel(mainViewModel)
        mainViewModel.getAllUser()
        userAdapter.onItemClick = OnItemClick { item, _ ->
            val bundle = Bundle().apply {
                putString(RepositoriesUserFragment.ARG_NAME, item.login)
            }
            postNormal(EventNextFragment(RepositoriesUserFragment::class.java, bundle, true))
        }
    }

    private fun callbackViewModel(vm: MainViewModel) {
        addDispose(vm.rxUsers.subscribe { userAdapter.items = it.toMutableList() },
            vm.rxMessage.subscribe { toast(it) },
            vm.isLoading.subscribe { if (it) binding.rcvUserGit.enableRefresh() else binding.rcvUserGit.cancelRefresh() })
    }
}