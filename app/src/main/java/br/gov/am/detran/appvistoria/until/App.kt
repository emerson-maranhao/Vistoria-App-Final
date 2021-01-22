package br.gov.am.detran.appvistoria.until

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import br.gov.am.detran.appvistoria.modules.*
import br.gov.am.detran.appvistoria.session.UserPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        //UserPreferences.init(this)
        UserPreferences.init(this@App)
        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    appModule,
                    viewModelModule,
                    loginModelModule,
                    repositoryModule,
                    databaseModule,
                    searchModelModule,
                    addModelModule
                )
            )
        }
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val themePref = sharedPreferences.getString("themePref", ThemeHelper.DEFAULT_MODE)
        ThemeHelper.applyTheme(themePref!!)


    }

}
