package com.kohuyn.basemvvm.ui.repo

import android.os.Bundle
import com.core.BaseFragment
import com.event.EventNextFragment
import com.kohuyn.basemvvm.databinding.FragmentRepositoriesBinding
import com.kohuyn.basemvvm.ui.repo.adapter.RepositoryAdapter
import com.utils.ext.argument
import com.utils.ext.postNormal
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Created by KOHuyn on 2/1/2021
 */
class RepositoriesUserFragment : BaseFragment<FragmentRepositoriesBinding>() {
    override fun getLayoutBinding(): FragmentRepositoriesBinding =
        FragmentRepositoriesBinding.inflate(layoutInflater)

    private val vm by viewModel<RepositoriesViewModel>()

    private val reposAdapter by lazy { RepositoryAdapter() }
    private val name by argument(ARG_NAME,"")

    companion object{
        const val ARG_NAME = "ARG_NAME"
        fun openFragment(name:String){
            val bundle = Bundle().apply {
                putString(ARG_NAME, name)
            }
            postNormal(EventNextFragment(RepositoriesUserFragment::class.java, bundle, true))
        }
    }

    override fun updateUI(savedInstanceState: Bundle?) {
        setUpRcv(binding.rcvRepos,reposAdapter)
        callbackViewModel()
        vm.getRepos(name)
    }

    private fun callbackViewModel() {
        addDispose(vm.rxRepositories.subscribe { reposAdapter.items = it.toMutableList() },
            vm.rxMessage.subscribe { toast(it) },
            vm.isLoading.subscribe { if (it) showDialog() else hideDialog() })
    }
}