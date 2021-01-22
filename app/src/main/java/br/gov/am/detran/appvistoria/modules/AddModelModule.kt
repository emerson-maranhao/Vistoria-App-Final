package br.gov.am.detran.appvistoria.modules


import br.gov.am.detran.appvistoria.presentation.ui.add.AddSurveyViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val addModelModule = module {

    // Specific viewModel pattern to tell Koin how to build CountriesViewModel
    viewModel {
        AddSurveyViewModel(repository = get())
    }

}