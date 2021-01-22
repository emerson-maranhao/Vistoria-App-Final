package br.gov.am.detran.appvistoria.repository

import br.gov.am.detran.appvistoria.domain.*
import br.gov.am.detran.appvistoria.network.ApiHelper

class Repository(private val apiHelper: ApiHelper) {
    suspend fun getSurveys() = apiHelper.getSurveys()
    suspend fun getVehicle(vehicle: VehicleRequest) = apiHelper.getVehicle(vehicle)
    suspend fun setSurvey(requestBodySurvey: RequestBodySurvey) = apiHelper.setSurvey(requestBodySurvey)
    suspend fun getLogin(user: User) = apiHelper.getLogin(user)



}