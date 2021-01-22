package br.gov.am.detran.appvistoria.domain


data class ResponseUser(
    var token: String = "",
    var cpf: String = "",
    var userName: String = "",
    var exp: String = "",
    var userId: String = "",
    var iat: String = "",
    var email: String = ""
)


