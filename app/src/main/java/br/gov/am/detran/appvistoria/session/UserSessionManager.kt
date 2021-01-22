package br.gov.am.detran.appvistoria.session

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import br.gov.am.detran.appvistoria.presentation.ui.auth.LoginActivity


class UserSessionManager(context: Context) {
    // Shared Preferences reference
    var pref: SharedPreferences


    // Editor reference for Shared preferences
    var editor: SharedPreferences.Editor

    // Context
    var _context: Context

    // Shared pref mode
    var PRIVATE_MODE = 0

    //Create login session
    fun createUserLoginSession(
        cpf: String?,
        userName: String?,
        exp: Int?,
        userId: String?,
        iat: Int?,
        email: String?,
        token: String?

    ) {
        // Storing login value as TRUE
        editor.putBoolean(IS_USER_LOGIN, true)

        // Storing name in pref
        editor.putString(KEY_TOKEN, token)
        editor.putString(KEY_CPF, cpf)
        editor.putString(KEY_USERNAME, userName)
        editor.putString(KEY_EXP, exp.toString())
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_IAT, iat.toString())
        editor.putString(KEY_EMAIL, email)

        // commit changes
        editor.commit()
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     */
    fun checkLogin(): Boolean {
        // Check login status
        if (!isUserLoggedIn) {

            // user is not logged in redirect him to Login Activity
            val i = Intent(_context, LoginActivity::class.java)

            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // Add new Flag to start new Activity
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            // Staring Login Activity
            _context.startActivity(i)
            return true
        }
        return false
    }//Use hashmap to store user credentials

    // user name

    // user email id

    // return user

    /**
     * Get stored session data
     */
    val userDetails: HashMap<String, String?>
        get() {

            //Use hashmap to store user credentials
            val user = HashMap<String, String?>()

            // user name
            user[KEY_TOKEN] =
                pref.getString(KEY_TOKEN, null)
            user[KEY_CPF] =
                pref.getString(KEY_CPF, null)
            user[KEY_EXP] =
                pref.getString(KEY_EXP, null)
            user[KEY_USER_ID] =
                pref.getString(KEY_USER_ID, null)
            user[KEY_IAT] =
                pref.getString(KEY_IAT, null)
            user[KEY_EMAIL] =
                pref.getString(KEY_EMAIL, null)

            // return user
            return user
        }

    /**
     * Clear session details
     */
    fun logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear()
        editor.commit()

        // After logout redirect user to Login Activity
        val i = Intent(_context, LoginActivity::class.java)

        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        // Add new Flag to start new Activity
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        // Staring Login Activity
        _context.startActivity(i)
    }

    fun getValueString(KEY_NAME: String): String? {

        return pref.getString(KEY_NAME, null)


    }

    private val callback: SharedPreferences.OnSharedPreferenceChangeListener
        get() = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences: SharedPreferences, s: String ->
            Log.i("Updated:", s)

        }

    // Check for login
    val isUserLoggedIn: Boolean
        get() = pref.getBoolean(IS_USER_LOGIN, false)

    companion object {
        // Sharedpref file name
        private const val PREFER_NAME = "UserSession"

        // All Shared Preferences Keys
        private const val IS_USER_LOGIN = "IsUserLoggedIn"

        // User name (make variable public to access from outside)
        const val KEY_TOKEN = "token"
        const val KEY_CPF = "cpf"
        const val KEY_USERNAME = "username"
        const val KEY_EXP = "exp"
        const val KEY_USER_ID = "userId"
        const val KEY_IAT = "iat"
        const val KEY_EMAIL = "email"

    }

    // Constructor
    init {
        _context = context
        pref = _context.getSharedPreferences(UserSessionManager.Companion.PREFER_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }
}