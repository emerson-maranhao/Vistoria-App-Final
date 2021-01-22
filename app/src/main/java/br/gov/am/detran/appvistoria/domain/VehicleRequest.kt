package br.gov.am.detran.appvistoria.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize

data class VehicleRequest(
    var placa: String?="",
    var chassi: String?="",
    var bNacional: String=""

) : Parcelable

