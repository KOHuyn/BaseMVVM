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
import com.utils.ext.setOnLoadMoreListener
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
        vm.getUserFromDbLimit(true)
        setupView()
        handleClick()
    }

    private fun setupView() {
        toast("Bạn đã vào app ${vm.countOpenApp} lần ^^")
        vm.countOpenApp++
        setUpRcv(binding.rcvUserGit, userAdapter)
        binding.rcvUserGit.addItemDecoration(
            MarginItemDecoration(resources.getDimensionPixelOffset(R.dimen.space_4))
        )
        userAdapter.attachWithRefreshLayout(binding.refreshLayout)
    }

    private fun handleClick() {
        userAdapter.onItemClick = OnItemClick { item, _ ->
            item.login?.let { RepositoriesUserFragment.openFragment(it) } ?: toast("Error unknown")
        }
        binding.refreshLayout.setOnRefreshListener {
            if (isDataWithDb) {
                vm.getUserFromDbLimit(isRefreshing = true)
            } else {
                vm.getAllUserApi(true)
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
                userAdapter.setData(it)
            },
            vm.rxUsersWithPage.subscribe {
                val items = it.first
                val pageResponse = it.second
                page.applyPageResponse(pageResponse)
                if (page.currentPage == 1) {
                    userAdapter.setData(items)
                } else {
                    userAdapter.addDataLoadMore(items)
                }
            },
            vm.rxStatusRcv.subscribe { userAdapter.updateStatusAdapter(it) },
            vm.rxMessage.subscribe {
                toast(it)
            },
            vm.isLoading.subscribe {
                if (it) showDialog() else hideDialog()
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
                            vm.getAllUserApi(false)
                            isDataWithDb = false
                        }
                } else {
                    vm.getAllUserApi(false)
                    isDataWithDb = false
                }
            }
        )
    }
}