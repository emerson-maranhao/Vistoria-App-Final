package br.gov.am.detran.appvistoria.repository

import br.gov.am.detran.appvistoria.domain.*
import br.gov.am.detran.appvistoria.until.AppResult

interface SurveysRepository {
    suspend fun getSurveys(): AppResult<List<Survey>>
   suspend fun getVehicle(vehicle: VehicleRequest): Vehicle?
    suspend fun setSurvey(requestBodySurvey: RequestBodySurvey): ResponseSurvey?
    suspend fun getLogin(user: User): ResponseUser?
}