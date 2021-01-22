package br.gov.am.detran.appvistoria.network

import br.gov.am.detran.appvistoria.domain.*
import retrofit2.Response

interface ApiHelper {
    suspend fun getSurveys() : Response<List<Survey>>

    suspend fun getVehicle(vehicle: VehicleRequest): Vehicle?

    suspend fun getLogin(user: User):ResponseUser?
    suspend fun setSurvey(requestBodySurvey: RequestBodySurvey):ResponseSurvey?

}