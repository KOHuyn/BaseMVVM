package com.kohuyn.basemvvm.di

import com.kohuyn.basemvvm.ui.main.MainViewModel
import com.kohuyn.basemvvm.ui.repo.RepositoriesViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by KOHuyn on 1/29/2021
 */

val view_module: Module = module {
    viewModel { MainViewModel(get(), get(), get()) }
    viewModel { RepositoriesViewModel(get(), get(), get()) }
}