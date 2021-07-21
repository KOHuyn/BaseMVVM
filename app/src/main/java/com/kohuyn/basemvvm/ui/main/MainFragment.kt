package com.kohuyn.basemvvm.ui.main

import android.os.Bundle
import com.core.BaseFragment
import com.core.OnItemClick
import com.kohuyn.basemvvm.R
import com.kohuyn.basemvvm.data.model.Page
import com.kohuyn.basemvvm.databinding.FragmentMainBinding
import com.kohuyn.basemvvm.ui.dialog.YesNoDialog
import com.kohuyn.basemvvm.ui.main.adapter.UserAdapter
import com.kohuyn.basemvvm.ui.repo.RepositoriesUserFragment
import com.kohuyn.basemvvm.ui.utils.MarginItemDecoration
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by KOHuyn on 1/29/2021
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun getLayoutBinding(): FragmentMainBinding =
        FragmentMainBinding.inflate(layoutInflater)

    private val vm by viewModel<MainViewModel>()

    //demo paging recyclerView
    private var isDataWithDb: Boolean = false

    //custom fake page in db
    private val page by lazy { Page() }

    private val userAdapter by lazy { UserAdapter() }

    //show popup once
    private var isShowPopupSaveDb = true

    override fun updateUI(savedInstanceState: Bundle?) {
        callbackViewModel()
        setupView()
        handleClick()
    }

    private fun setupView() {
        toast("Bạn đã vào app ${vm.countOpenApp} lần ^^")
        vm.countOpenApp++
        binding.rcvUserGit.setUpRcv(userAdapter)
        binding.rcvUserGit.getRcv().addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelOffset(R.dimen.space_4))
        )
    }

    private fun handleClick() {
        userAdapter.onItemClick = OnItemClick { item, _ ->
            item.login?.let { RepositoriesUserFragment.openFragment(it) } ?: toast("Error unknown")
        }
        binding.rcvUserGit.setOnRefreshListener {
            if (isDataWithDb) {
                vm.getUserFromDbLimit()
            } else {
                vm.getAllUserApi()
            }
        }
        binding.rcvUserGit.setOnLoadMoreListener {
            if (isDataWithDb) {
                page.onLoadMore { nextPage ->
                    toast("Page $nextPage")
                    vm.getUserFromDbLimit(false, nextPage)
                }
            }
        }
    }

    private fun callbackViewModel() {
        addDispose(
            vm.rxUsers.subscribe {
                userAdapter.items = it.toMutableList()
            },
            vm.rxUsersWithPage.subscribe {
                val items = it.first
                val pageResponse = it.second
                page.applyPageResponse(pageResponse)
                if (page.currentPage == 1) {
                    binding.rcvUserGit.setShowViewNoData(items.isEmpty())
                    userAdapter.items = items
                } else {
                    userAdapter.items.addAll(items)
                }
                binding.rcvUserGit.updateLayout()
            },
            vm.rxMessage.subscribe {
                toast(it)
            },
            vm.isLoading.subscribe {
                if (page.isLoading) {
                    binding.rcvUserGit.isLoadMore = it
                } else {
                    binding.rcvUserGit.isRefreshing = it
                }
            },
            vm.rxIsSaveDb.subscribe {
                if (isShowPopupSaveDb) {
                    isShowPopupSaveDb = false
                    YesNoDialog.Builder(parentFragmentManager)
                        .setTitle("Lưu dữ liệu vào db")
                        .setMessage("Bạn có muốn lưu dữ liệu vào db không?")
                        .setButtonAccept("Có") {
                            vm.saveAllUserInDb(it)
                        }
                        .setButtonCancel("Không") {}
                }
            }, vm.rxUserInDbAvailable.subscribe { isAvailable ->
                if (isAvailable) {
                    YesNoDialog.Builder(parentFragmentManager)
                        .setTitle("Lấy dữ liệu từ db")
                        .setMessage("Bạn có muốn lấy dữ liệu từ db không?")
                        .setButtonAccept("Có") {
                            vm.getUserFromDbLimit()
                            isDataWithDb = true
                        }
                        .setButtonCancel("Không") {
                            vm.getAllUserApi()
                            isDataWithDb = false
                        }
                } else {
                    vm.getAllUserApi()
                    isDataWithDb = false
                }
            }
        )
    }
}