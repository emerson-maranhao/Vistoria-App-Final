package br.gov.am.detran.appvistoria.session

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object UserPreferences {
    private const val NAME = "AComputerEngineer"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    //SharedPreferences variables
    private val IS_LOGIN = Pair("is_login", false)
    private val USERNAME = Pair("name", "")
    private val PASSWORD = Pair("password", "")
    private val TOKEN = Pair("token", "")
    private val EMAIL = Pair("email", "")
    private val EXP = Pair("exp", 0)
    private val IAT = Pair("iat", 0)
    private val USERID = Pair("user_id", "")
    private val CPF = Pair("cpd", "")


    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    fun clear(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.clear()
        editor.commit()
    }

    //an inline function to put variable and save it
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    //SharedPreferences variables getters/setters
    var isLogin: Boolean
        get() = preferences.getBoolean(IS_LOGIN.first, IS_LOGIN.second)
        set(value) = preferences.edit {
            it.putBoolean(IS_LOGIN.first, value)
        }

    var username: String
        get() = preferences.getString(USERNAME.first, USERNAME.second) ?: ""
        set(value) = preferences.edit {
            it.putString(USERNAME.first, value)
        }

    var password: String
        get() = preferences.getString(PASSWORD.first, PASSWORD.second) ?: ""
        set(value) = preferences.edit {
            it.putString(PASSWORD.first, value)
        }

    var token: String
        get() = preferences.getString(TOKEN.first, TOKEN.second) ?: ""
        set(value) = preferences.edit {
            it.putString(TOKEN.first, value)
        }
    var email: String
        get() = preferences.getString(EMAIL.first, EMAIL.second) ?: ""
        set(value) = preferences.edit {
            it.putString(EMAIL.first, value)
        }
    var exp: Int
        get() = preferences.getInt(EXP.first, EXP.second)
        set(value) = preferences.edit {
            it.putInt(EXP.first, value)
        }
    var iat: Int
        get() = preferences.getInt(IAT.first, IAT.second)
        set(value) = preferences.edit {
            it.putInt(IAT.first, value)
        }
    var userId: String
        get() = preferences.getString(USERID.first, USERID.second) ?: ""
        set(value) = preferences.edit {
            it.putString(USERID.first, value)
        }
    var cpf: String
        get() = preferences.getString(CPF.first, CPF.second) ?: ""
        set(value) = preferences.edit {
            it.putString(CPF.first, value)
        }


}
