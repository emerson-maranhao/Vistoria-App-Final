package br.gov.am.detran.appvistoria.modules


import br.gov.am.detran.appvistoria.presentation.ui.auth.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModelModule = module {

    // Specific viewModel pattern to tell Koin how to build CountriesViewModel
    viewModel {
        LoginViewModel(repository = get(), context = get())
    }

}