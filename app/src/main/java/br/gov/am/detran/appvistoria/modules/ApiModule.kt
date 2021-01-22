package br.gov.am.detran.appvistoria.modules

import br.gov.am.detran.appvistoria.network.ApiService
import org.koin.dsl.module
import retrofit2.Retrofit

//val apiModule = module {
//
//    fun provideSurveysApi(retrofit: Retrofit): ApiService {
//        return retrofit.create(ApiService::class.java)
//    }
//    single { provideSurveysApi(get()) }
//
//}