package br.gov.am.detran.appvistoria.modules

import android.content.Context
import br.gov.am.detran.appvistoria.BuildConfig
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.network.ApiHelper
import br.gov.am.detran.appvistoria.network.ApiHelperImpl
import br.gov.am.detran.appvistoria.network.ApiService
import br.gov.am.detran.appvistoria.network.OAuthInterceptor
import br.gov.am.detran.appvistoria.session.UserPreferences
import br.gov.am.detran.appvistoria.until.NetworkManager
import br.gov.am.detran.appvistoria.until.ignoreAllSSLErrors
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single { provideOkHttpClient() }
    // single { provideRetrofit1(get(), Constants.API_DETRAN) }
    single { provideRetrofit(get(), BuildConfig.API_DETRAN_VISTORIA) }
    single { provideApiService(get()) }
    single { provideNetworkHelper(androidContext()) }

    single<ApiHelper> {
        return@single ApiHelperImpl(get())
    }
}

private fun provideNetworkHelper(context: Context) = NetworkManager.isOnline(context)

private fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .ignoreAllSSLErrors()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(OAuthInterceptor("Bearer", UserPreferences.token))
        .build()
}else{
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
    OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(OAuthInterceptor("Bearer", UserPreferences.token))
        .build()
}
//else OkHttpClient
//    .Builder()
//    .build()



private fun provideRetrofit(
    okHttpClient: OkHttpClient,
    BASE_URL: String
): Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

private fun provideApiService(retrofit: Retrofit): ApiService {
    return retrofit.create(ApiService::class.java)

}


