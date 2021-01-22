package br.gov.am.detran.appvistoria.domain

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "survey_table")
data class Vehicle(

    @PrimaryKey
    @ColumnInfo(name = "placa")
    var placa: String = "",
    @ColumnInfo(name = "renavam")
    var renavam: String = "",
    @ColumnInfo(name = "numeroMotor")
    var numeroMotor: String = "",
    @ColumnInfo(name = "ufEmplacamento")
    var ufEmplacamento: String = "",
    @ColumnInfo(name = "chassi")
    var chassi: String = "",
    @ColumnInfo(name = "situacao")
    var situacao: String = "",
    @ColumnInfo(name = "descricaoMarca")
    var descricaoMarca: String = "",
    @ColumnInfo(name = "descricaoMarcaFabricante")
    var descricaoMarcaFabricante: String = "",
    @ColumnInfo(name = "descricaoModelo")
    var descricaoModelo: String = "",
    @ColumnInfo(name = "descricaoCor")
    var descricaoCor: String = "",
    @ColumnInfo(name = "descricaoTipo")
    var descricaoTipo: String = "",
    @ColumnInfo(name = "descricaoCategoria")
    var descricaoCategoria: String = "",
    @ColumnInfo(name = "anoFabricacao")
    var anoFabricacao: String = "",
    @ColumnInfo(name = "anoModelo")
    var anoModelo: String = "",
    @ColumnInfo(name = "descricaoCombustivel")
    var descricaoCombustivel: String = "",
    @ColumnInfo(name = "procedencia")
    var procedencia: String = "",
    @ColumnInfo(name = "quantidadePotencia")
    var quantidadePotencia: String = "",
    @ColumnInfo(name = "anoUltimoLicenciamento")
    var anoUltimoLicenciamento: String = "",
    @ColumnInfo(name = "dataVencimentoLicenciamento")
    var dataVencimentoLicenciamento: String = "",
    @ColumnInfo(name = "municipioEmplacamento")
    var municipioEmplacamento: String = "",
    @ColumnInfo(name = "nomeProprietario")
    var nomeProprietario: String = "",
    @ColumnInfo(name = "restricoes")
    var restricoes: Array<String>,
    @ColumnInfo(name = "numeroCRV")
    var numeroCRV: String = "",
    @ColumnInfo(name = "numeroCRLV")
    var numeroCRLV: String = "",
    @ColumnInfo(name = "numeroLacre")
    var numeroLacre: String = ""


    ) : Parcelable {
        constructor(
            placa: String?,
            chassi: String?
        ) : this(
            placa = placa!!,
            renavam = "",
        numeroMotor = "",
        ufEmplacamento = "",
        chassi = chassi!!,
        situacao = "",
        descricaoMarca = "",
        descricaoMarcaFabricante = "",
        descricaoModelo = "",
        descricaoCor = "",
        descricaoTipo = "",
        descricaoCategoria = "",
        anoFabricacao = "",
        anoModelo = "",
        descricaoCombustivel = "",
        procedencia = "",
        quantidadePotencia = "",
        anoUltimoLicenciamento = "",
        dataVencimentoLicenciamento = "",
        municipioEmplacamento = "",
        nomeProprietario = "",
        restricoes = arrayOf(""),
        numeroCRV = "",
        numeroCRLV = "",
        numeroLacre = ""
    )

}


