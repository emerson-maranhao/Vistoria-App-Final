package br.gov.am.detran.appvistoria.modules

import android.os.Build
import br.gov.am.detran.appvistoria.BuildConfig
import br.gov.am.detran.appvistoria.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.BuildConfig.BUILD_TYPE
import org.koin.android.BuildConfig.DEBUG
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    val connectTimeout : Long = 40// 20s
    val readTimeout : Long  = 40 // 20s

    fun provideHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)
            .readTimeout(readTimeout, TimeUnit.SECONDS)
        if (DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor)
        }
        okHttpClientBuilder.build()
        return okHttpClientBuilder.build()
    }

    fun provideRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
    }

    single { provideHttpClient() }
    single {
        //val baseUrl = androidContext().getString(R.string.BASE_URL)
        //val baseUrl = androidContext().getString(R.string.API_DETRAN)
        val baseUrl = BuildConfig.API_DETRAN_VISTORIA

        provideRetrofit(get(), baseUrl)
    }
}