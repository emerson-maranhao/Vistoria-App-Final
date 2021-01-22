package br.gov.am.detran.appvistoria.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.gov.am.detran.appvistoria.domain.Survey

@Dao
interface SurveyDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun setSurvey(survey: List<Survey>)

    @Query("SELECT * from survey_table where user =:user")
    fun getSurveys(user: String): List<Survey>

    @Query("SELECT * from survey_table where license_plate =:placa")
    fun getSurvey(placa: String):Survey

    @Query("DELETE  from survey_table")
    fun deleteAll()
}