package br.gov.am.detran.appvistoria.network

import br.gov.am.detran.appvistoria.domain.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

//mapeamento das chamadas http para o servidor

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun userLogin(

        @Body user: User
    ): br.gov.am.detran.appvistoria.domain.ResponseUser?

    @GET("listallUser")
    suspend fun getSurveys(
    ): Response<List<Survey>>

    @Multipart
    @POST("api/file")
    suspend fun insertSurvey(
        @Part("license_plate") license_plate: RequestBody?,
        @Part("renavam") renavam: RequestBody?,
        @Part("year") year: RequestBody?,
        @Part("brand") brand: RequestBody?,
        @Part("type") type: RequestBody?,
        @Part("color") color: RequestBody?,
        @Part("kind") kind: RequestBody?,
        @Part("uf") uf: RequestBody?,
        @Part("chassis") chassis: RequestBody?,
        @Part("engine") engine: RequestBody?,
        @Part chassis_photo: MultipartBody.Part?,
        @Part("chassis_obs") chassis_obs: RequestBody?,
        @Part engine_photo: MultipartBody.Part?,
        @Part("engine_obs") engine_obs: RequestBody?,
        @Part back_photo: MultipartBody.Part?,
        @Part("back_obs") back_obs: RequestBody?,
        @Part("electric_pendency") electricPendency: RequestBody?,
        @Part("external_pendency") externalPendency: RequestBody?,
        @Part("internal_pendency") internalPendency: RequestBody?,
        @Part("mandatory_pendency") mandatoryPendency: RequestBody?,
        @Part("survey_place") survey_place: RequestBody?,
        @Part("status") status: RequestBody?,
        @Part("latitude") latitude: RequestBody?,
        @Part("longitude") longitude: RequestBody?

    ): ResponseSurvey?


    @Headers("Content-Type: application/json")
    @POST("search")
    suspend fun getVehicle(
        @Body vehicleRequest: VehicleRequest
        ): Vehicle?



    @Multipart
    @POST("file")
    fun uploadImage(
        @Part("license_plate") license_plate: RequestBody?,
        @Part("year") year: RequestBody?,
        @Part("brand") brand: RequestBody?,
        @Part("type") type: RequestBody?,
        @Part("kind") kind: RequestBody?,
        @Part("color") color: RequestBody?,
        @Part("uf") uf: RequestBody?,
        @Part("chassis") chassis: RequestBody?,
        @Part("engine") engine: RequestBody?,  //@Part MultipartBody.Part chassis_photo,
        @Part("chassis_photo\"; filename=\"myfile1.jpg\" ") chassis_photo: RequestBody?,
        @Part("chassis_obs") chassis_obs: RequestBody?,  //@Part MultipartBody.Part engine_photo,
        @Part("engine_photo\"; filename=\"myfile2.jpg\" ") engine_photo: RequestBody?,
        @Part("engine_obs") engine_obs: RequestBody?,
        @Part("back_photo\"; filename=\"myfile3.jpg\" ") back_photo: RequestBody?,  // @Part MultipartBody.Part back_photo,
        @Part("back_obs") back_obs: RequestBody?,  //@Part MultipartBody.Part odometer_photo,
        @Part("odometer_photo\"; filename=\"myfile4.jpg\" ") odometer_photo: RequestBody?,
        @Part("odometer_obs") odometer_obs: RequestBody?,
        @Part("survey_place") survey_place: RequestBody?,
        @Part("status") status: RequestBody?
    ): Call<Survey>?

}
