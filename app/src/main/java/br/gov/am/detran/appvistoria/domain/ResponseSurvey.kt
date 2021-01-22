package br.gov.am.detran.appvistoria.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class ResponseSurvey(
    var message: String? = "",
    var _id: String? = ""

):Parcelable


