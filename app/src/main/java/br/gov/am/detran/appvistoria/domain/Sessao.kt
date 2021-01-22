package br.gov.am.detran.appvistoria.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
@Parcelize
data class Sessao(
    val cpf: String?,
    val userName: String?,
    val exp: Int?,
    val userId: String?,
    val iat: Int?,
    val email: String?,
    val token: String?
): Parcelable