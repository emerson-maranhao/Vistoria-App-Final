package br.gov.am.detran.appvistoria.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
@Parcelize
@Entity(tableName = "survey_table")
data class Survey(

    @PrimaryKey
    @ColumnInfo(name = "_id")
    var _id: String = "",

    @ColumnInfo(name = "user")
    var user: String = "",

    @ColumnInfo(name = "license_plate")
    var license_plate: String = "",

    @ColumnInfo(name = "renavam")
    var renavam: String = "",

    @ColumnInfo(name = "year")
    var year: String = "",

    @ColumnInfo(name = "brand")
    var brand: String = "",

    @ColumnInfo(name = "type")
    var type: String = "",

    @ColumnInfo(name = "color")
    var color: String = "",

    @ColumnInfo(name = "kind")
    var kind: String = "",

    @ColumnInfo(name = "uf")
    var uf: String = "",

    @ColumnInfo(name = "chassis")
    var chassis: String = "",

    @ColumnInfo(name = "engine")
    var engine: String = "",

    @ColumnInfo(name = "chassis_photo")
    var chassis_photo: String? = "",

    @ColumnInfo(name = "chassis_obs")
    var chassis_obs: String? = "",

    @ColumnInfo(name = "engine_photo")
    var engine_photo: String? = "",

    @ColumnInfo(name = "engine_obs")
    var engine_obs: String? = "",

    @ColumnInfo(name = "back_photo")
    var back_photo: String? = "",

    @ColumnInfo(name = "back_obs")
    var back_obs: String? = "",

    @ColumnInfo(name = "electric_pendency")
    var electric_pendency: String? = "",

    @ColumnInfo(name = "external_pendency")
    var external_pendency: String? = "",

    @ColumnInfo(name = "internal_pendency")
    var internal_pendency: String? = "",

    @ColumnInfo(name = "mandatory_pendency")
    var mandatory_pendency: String? = "",

    @ColumnInfo(name = "survey_place")
    var survey_place: String = "",

    @ColumnInfo(name = "status")
    var status: String = "",

    @ColumnInfo(name = "data_insert")
    var data_insert: String,

    @ColumnInfo(name = "date_now")
    var date_now: String,

    @ColumnInfo(name = "nLaudo")
    var nLaudo: String?,

    @ColumnInfo(name = "latitude")
    var latitude: String?,

    @ColumnInfo(name = "longitude")
    var longitude: String?

) : Parcelable
