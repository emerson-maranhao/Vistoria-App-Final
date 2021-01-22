package br.gov.am.detran.appvistoria.modules

import br.gov.am.detran.appvistoria.presentation.ui.home.HomeViewModel


import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {


    // Specific viewModel pattern to tell Koin how to build CountriesViewModel
    viewModel {
        HomeViewModel(repository = get(), context = get(), dao = get())
    }

}