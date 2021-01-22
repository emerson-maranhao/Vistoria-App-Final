package br.gov.am.detran.appvistoria.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize

data class User(
    var cpf: String = "",
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var matricula: String = "",
    var imei: String = ""


) : Parcelable {
    constructor(
        cpf: String?,
        password: String?, imei: String?
    ) : this(
        cpf = cpf!!,
        password = password!!,
        imei = imei!!
    )
}

