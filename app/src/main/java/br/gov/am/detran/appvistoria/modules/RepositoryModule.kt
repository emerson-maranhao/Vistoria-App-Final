package br.gov.am.detran.appvistoria.modules

import android.content.Context
import br.gov.am.detran.appvistoria.dao.SurveyDao
import br.gov.am.detran.appvistoria.network.ApiService
import br.gov.am.detran.appvistoria.repository.Repository
import br.gov.am.detran.appvistoria.repository.SurveysRepository
import br.gov.am.detran.appvistoria.repository.SurveysRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    fun provideCountryRepository(api: ApiService, context: Context, dao : SurveyDao): SurveysRepository {
        return SurveysRepositoryImpl(api, context, dao)
    }
    single { provideCountryRepository(get(), androidContext(), get()) }

//    single {
//        Repository(get())
//    }
}