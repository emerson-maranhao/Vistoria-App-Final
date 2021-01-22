package br.gov.am.detran.appvistoria.modules


import br.gov.am.detran.appvistoria.presentation.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModelModule = module {

    // Specific viewModel pattern to tell Koin how to build CountriesViewModel
    viewModel {
        SearchViewModel(repository = get(), context = get())
    }

}