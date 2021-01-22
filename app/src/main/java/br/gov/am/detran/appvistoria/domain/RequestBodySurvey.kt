package br.gov.am.detran.appvistoria.domain


import okhttp3.MultipartBody
import okhttp3.RequestBody

data class RequestBodySurvey(

    var license_plate: RequestBody,
    var renavam: RequestBody,
    var year: RequestBody?,
    var brand: RequestBody?,
    var type: RequestBody?,
    var color: RequestBody?,
    var kind: RequestBody?,
    var uf: RequestBody?,
    var chassis: RequestBody?,
    var engine: RequestBody?,
    var chassis_photo: MultipartBody.Part?,
    var chassis_obs: RequestBody?,
    var engine_photo: MultipartBody.Part?,
    var engine_obs: RequestBody?,
    var back_photo: MultipartBody.Part?,
    var back_obs: RequestBody?,
    var electric_dependency: RequestBody?,
    var external_dependency: RequestBody?,
    var internal_dependency: RequestBody?,
    var mandatory_dependency: RequestBody?,
    var survey_place: RequestBody?,
    var status: RequestBody?,
    var latitude:RequestBody?,
    var longitude:RequestBody?
)
