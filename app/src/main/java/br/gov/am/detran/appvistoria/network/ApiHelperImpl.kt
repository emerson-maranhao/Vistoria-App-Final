package br.gov.am.detran.appvistoria.network

import br.gov.am.detran.appvistoria.domain.*
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getSurveys(): Response<List<Survey>> = apiService.getSurveys()

    override suspend fun getVehicle(vehicle: VehicleRequest): Vehicle? {
        return apiService.getVehicle(vehicle)
    }

    override suspend fun getLogin(user: User) = apiService.userLogin(user)

    override suspend fun setSurvey(requestBodySurvey: RequestBodySurvey): ResponseSurvey? {
        return apiService.insertSurvey(
            license_plate = requestBodySurvey.license_plate,
            renavam = requestBodySurvey.renavam,
            year = requestBodySurvey.year,
            brand = requestBodySurvey.brand,
            type = requestBodySurvey.type,
            color = requestBodySurvey.color,
            kind = requestBodySurvey.kind,
            uf = requestBodySurvey.uf,
            chassis = requestBodySurvey.chassis,
            engine = requestBodySurvey.engine,
            chassis_photo = requestBodySurvey.chassis_photo,
            chassis_obs = requestBodySurvey.chassis_obs,
            engine_photo = requestBodySurvey.engine_photo,
            engine_obs = requestBodySurvey.engine_obs,
            back_photo = requestBodySurvey.back_photo,
            back_obs = requestBodySurvey.back_obs,
            electricPendency = requestBodySurvey.electric_dependency,
            externalPendency = requestBodySurvey.external_dependency,
            internalPendency = requestBodySurvey.internal_dependency,
            mandatoryPendency = requestBodySurvey.mandatory_dependency,
            survey_place = requestBodySurvey.survey_place,
            status = requestBodySurvey.status,
            latitude = requestBodySurvey.latitude,
            longitude = requestBodySurvey.longitude
        )
    }
}

