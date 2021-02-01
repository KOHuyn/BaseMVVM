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
        setUpRcv(binding.rcvUserGit, userAdapter)
        binding.rcvUserGit.addItemDecoration(
            MarginItemDecoration(
                resources.getDimension(R.dimen.space_4).toInt()
            )
        )
        callbackViewModel(mainViewModel)
        mainViewModel.getAllUser()
        userAdapter.onItemClick = OnItemClick { _, position ->
            val bundle = Bundle().apply {
                putString(RepositoriesUserFragment.ARG_NAME, userAdapter.items[position].login)
            }
            postNormal(EventNextFragment(RepositoriesUserFragment::class.java, bundle, true))
        }
    }

    private fun callbackViewModel(vm: MainViewModel) {
        addDispose(vm.rxUsers.subscribe { userAdapter.items = it.toMutableList() },
            vm.rxMessage.subscribe { toast(it) },
            vm.isLoading.subscribe { if (it) showDialog() else hideDialog() })
    }
}