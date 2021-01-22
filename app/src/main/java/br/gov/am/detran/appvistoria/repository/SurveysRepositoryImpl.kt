package br.gov.am.detran.appvistoria.repository

import android.content.Context
import android.util.Log
import br.gov.am.detran.appvistoria.dao.SurveyDao
import br.gov.am.detran.appvistoria.domain.*
import br.gov.am.detran.appvistoria.network.ApiService
import br.gov.am.detran.appvistoria.session.UserPreferences
import br.gov.am.detran.appvistoria.until.AppResult
import br.gov.am.detran.appvistoria.until.NetworkManager.isOnline
import br.gov.am.detran.appvistoria.until.Utils
import br.gov.am.detran.appvistoria.until.noNetworkConnectivityError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SurveysRepositoryImpl(
    private val api: ApiService,
    private val context: Context,
    private val dao: SurveyDao,
) :
    SurveysRepository {

    override suspend fun getSurveys(): AppResult<List<Survey>> {
        if (isOnline(context)) {

            return try {
                val response = api.getSurveys()
                setSurveyDb(response.body()!!)

                if (response.isSuccessful) {
                    Log.i("Online:", "Success")
                    //save the data
                    withContext(Dispatchers.Main) {
                        Log.i("io:", "ioooo")

                        deleteSurveysDb()
                        setSurveyDb(response.body()!!)


                    }
                    Utils().handleSuccess(response)
                } else {
                    Log.i("Online:", "Error")

                    //Utils().handleApiError(response)
                    val r = getCountriesDataFromCache()

                    AppResult.Success(r)
                }
            } catch (e: Exception) {
                AppResult.Success(getCountriesDataFromCache())
            }
        } else {
            Log.i("Offline:", "Success")

            //check in db if the data exists
            val data = getCountriesDataFromCache()
            Log.d("Data", data.toString())
            return if (data.isNotEmpty()) {
                Log.d("Data", "from db")
                Log.d("Data", data.toString())
                AppResult.Success(data)
            } else
            //no network
                context.noNetworkConnectivityError()
        }
    }

    override suspend fun getVehicle(vehicle: VehicleRequest): Vehicle? {
        return api.getVehicle(vehicle)
        // return fakeVehicle()
    }

    override suspend fun setSurvey(requestBodySurvey: RequestBodySurvey): ResponseSurvey? {
        return api.insertSurvey(
            requestBodySurvey.license_plate,
            requestBodySurvey.renavam,
            requestBodySurvey.year,
            requestBodySurvey.brand,
            requestBodySurvey.type,
            requestBodySurvey.color,
            requestBodySurvey.kind,
            requestBodySurvey.uf,
            requestBodySurvey.chassis,
            requestBodySurvey.engine,
            requestBodySurvey.chassis_photo,
            requestBodySurvey.chassis_obs,
            requestBodySurvey.engine_photo,
            requestBodySurvey.engine_obs,
            requestBodySurvey.back_photo,
            requestBodySurvey.back_obs,
            requestBodySurvey.electric_dependency,
            requestBodySurvey.external_dependency,
            requestBodySurvey.internal_dependency,
            requestBodySurvey.mandatory_dependency,
            requestBodySurvey.survey_place,
            requestBodySurvey.status,
            requestBodySurvey.latitude,
            requestBodySurvey.longitude
        )
    }

    override suspend fun getLogin(user: User): ResponseUser? {

        return api.userLogin(user)
//        return fakeLogin()
    }

    private suspend fun getCountriesDataFromCache(): List<Survey> {
        return withContext(Dispatchers.IO) {
            dao.getSurveys(user = UserPreferences.userId)

        }
    }

    private suspend fun deleteSurveysDb() {
        return withContext(Dispatchers.IO) {
            dao.deleteAll()
        }
    }

    private suspend fun setSurveyDb(list: List<Survey>) {
        return withContext(Dispatchers.IO) {
            dao.setSurvey(list)
        }
    }

}

private fun fakeVehicle(): Vehicle {
    return Vehicle(
        "JXS0855",
        "23234234",
        "12334234",
        "AM",
        "2342hj324124h",
        "Em circulaçao",
        "VOLKs",
        "marca",
        "modelo",
        "cor",
        "tipo",
        "categoria",
        "anoF",
        "anoM",
        "combustivel",
        "procedencia",
        "potencia",
        "licenciamento",
        "data",
        "emplacamento",
        "proprietario",
        arrayOf("restricoes"),
        "crv",
        "crlv"
    )

}
private fun fakeLogin(): ResponseUser {
    return ResponseUser(
        "kjlkdsjfasjflkasjdflkjdsflkjas.kjlkdsjfasjflkasjdflkjdsflkjas.kjlkdsjfasjflkasjdflkjdsflkjas",
        "01323361200",
        "Emerson",
        "3423412",
        "123456",
        "231123",
        "emerson_maranhao@hotmail.com"
    )
}