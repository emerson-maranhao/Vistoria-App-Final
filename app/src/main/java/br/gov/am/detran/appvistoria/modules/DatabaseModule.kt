package br.gov.am.detran.appvistoria.modules
import android.app.Application
import androidx.room.Room
import br.gov.am.detran.appvistoria.dao.SurveyDao
import br.gov.am.detran.appvistoria.dao.SurveyDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {

    fun provideDatabase(application: Application): SurveyDatabase {
       return Room.databaseBuilder(application, SurveyDatabase::class.java, "surveys")
            .fallbackToDestructiveMigration()
            .build()
    }

    fun provideCountriesDao(database: SurveyDatabase): SurveyDao {
        return  database.surveyDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideCountriesDao(get()) }


}
