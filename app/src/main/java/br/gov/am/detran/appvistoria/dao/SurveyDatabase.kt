package br.gov.am.detran.appvistoria.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.gov.am.detran.appvistoria.domain.Survey

@Database(entities = [Survey::class], version = 2, exportSchema = false)
abstract class SurveyDatabase : RoomDatabase() {

    abstract fun surveyDao(): SurveyDao

//    companion object {
//
//        @Volatile
//        private var INSTANCE: SurveyDatabase? = null
//
//        fun getDatabase(context: Context): SurveyDatabase? {
//            if (INSTANCE == null) {
//                synchronized(SurveyDatabase::class.java) {
//                    if (INSTANCE == null) {
//                        INSTANCE = Room.databaseBuilder(
//                            context.applicationContext,
//                            SurveyDatabase::class.java, "survey_database"
//                        )
//                            .build()
//                    }
//                }
//            }
//            return INSTANCE
//        }
//    }
}