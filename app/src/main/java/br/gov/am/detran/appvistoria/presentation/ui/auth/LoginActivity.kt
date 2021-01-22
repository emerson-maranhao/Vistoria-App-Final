package br.gov.am.detran.appvistoria.presentation.ui.auth

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import br.gov.am.detran.appvistoria.BuildConfig
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.ActivityLoginBinding
import br.gov.am.detran.appvistoria.domain.Sessao
import br.gov.am.detran.appvistoria.domain.User
import br.gov.am.detran.appvistoria.presentation.MainActivity
import br.gov.am.detran.appvistoria.presentation.ui.home.Status
import br.gov.am.detran.appvistoria.session.UserPreferences
import br.gov.am.detran.appvistoria.until.Mask
import br.gov.am.detran.appvistoria.until.Utils
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()
    private var token: String? = ""
    private var name: String? = ""
    private var email: String? = ""
    private var iat: Int? = 0
    private var exp: Int? = 0
    private var cpfUser: String? = ""
    private var userId: String? = ""
    lateinit var progressBar: ProgressBar
    // var session: UserSessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Log.i(TAG, "OnCreate")

        progressBar = binding.progressBar
        if (isTokenValid()) {
            UserPreferences.isLogin
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {

            binding.etCpfLogin.addTextChangedListener(Mask.mask(getString(R.string.cpf_mask),
                binding.etCpfLogin))
            binding.btnLogin.setOnClickListener(View.OnClickListener {
                if (binding.etCpfLogin.text!!.isEmpty() || binding.etPasswordLogin.text!!.isEmpty()) {
                    Toast.makeText(this,
                        getString(R.string.usuario_senha_em_branco),
                        Toast.LENGTH_LONG).show()
                } else {
                    Utils().hideSoftKeyboard(this@LoginActivity, binding.etCpfLogin)

                    val cpf = (binding.etCpfLogin as TextInputEditText).text.toString()

                    val cpfNoMask = Mask.unmask(cpf)
                    val password = (binding.etPasswordLogin as TextInputEditText).text.toString()
                    if (binding.progressBar != null) {
                        val visibility =
                            if (binding.progressBar.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                        binding.progressBar.visibility = visibility
                        login(cpfNoMask!!, password)
                    }
                }


            })
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "OnStart")

    }

    override fun onResume() {
        super.onResume()
//        if (isTokenValid()) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }

        Log.i(TAG, "OnResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "OnPause")
//        preferences = getSharedPreferences(NAME1, MODE1)
////       token = preferences.getString("token", "")!!
//        val editor: SharedPreferences.Editor = preferences.edit()
//        editor.putString("token_value", token)
//        editor.putString("name_value", name)
//        editor.putString("email_value", email)
//        editor.commit()
//
//
    }


    override fun onStop() {
        super.onStop()
        Log.i(TAG, "OnStop")

        //  preferences = getSharedPreferences(NAME1, MODE1)
//       token = preferences.getString("token", "")!!
//        val editor: SharedPreferences.Editor = preferences.edit()
//        editor.putString("token_value", token)
//        editor.putString("name_value", name)
//        editor.putString("email_value", email)
//        editor.putInt("exp_value", exp!!)
//        editor.putInt("iat_value", iat!!)
//        editor.putString("userId_value", userId)
//        editor.putString("cpf_value", cpfUser)
//
//        editor.commit()
    }


    //    override fun onResume() {
//        super.onResume()
//        preferences = getSharedPreferences(NAME1, MODE1)
//        UserPreferences.isLogin = preferences.getBoolean("is_login", false)
//        if (!UserPreferences.isLogin) {
//            UserPreferences.isLogin
//            val intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    override fun onStart() {

//        super.onStart()
//        preferences = getSharedPreferences(NAME1, MODE1)
//
//        UserPreferences.isLogin = preferences.getBoolean("is_login", false)
//
//        if (!UserPreferences.isLogin) {
//            val intent = Intent(this, MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(intent)
//            finish()
//        }
//    }

    private fun login(cpf: String, password: String) {
        Log.i(TAG, "CPF: $cpf")
        Log.i(TAG, "Password: $password")
        token = getCredentials(cpf, password)
        Log.i(TAG, "login-token: $token")
    }

    private fun getCredentials(cpf: String, password: String): String? {
        val user: User = User(cpf, "", "", password, "", "")
        loginViewModel.getLogin(user).observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { response ->
                        Log.i(TAG, "Response: $response")

                        token = response.token
                        Log.i(TAG, "$token")
                        token?.let { it ->
                            val session = decodeToken(it)
                            Log.i(TAG, "Sessão: $session")
                            UserPreferences.token = session.token.toString()
                            UserPreferences.username = session.userName.toString()
                            UserPreferences.email = session.email.toString()
                            UserPreferences.exp = session.exp!!
                            UserPreferences.iat = session.iat!!
                            UserPreferences.userId = session.userId.toString()
                            UserPreferences.cpf = session.cpf.toString()

                            val intent = Intent(this, MainActivity::class.java)

                            this@LoginActivity.startActivity(intent)

                        }
                    }
                }

                Status.ERROR -> {
                    var error = it.message
                    Log.i(TAG, "Error: ${it.message}")

                    if (error!!.contains("HTTP 401 Unauthorized")) {
                        Log.i(TAG, "Error: ${it.message}")
                        progressBar.visibility = View.GONE
                        Toast.makeText(this,
                            getString(R.string.cpf_senha_invalidos),
                            Toast.LENGTH_LONG).show()

                    } else {
                        //recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        token = null
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
//                    Toast.makeText(
//                        this, it.message, Toast.LENGTH_LONG
//                    ).show()
                    }
                }
                Status.LOADING -> {
                    // progressBar.visibility = View.VISIBLE
                    // recyclerView.visibility = View.GONE
                }
            }
        })
        return token
    }

    private fun verifyToken(token: String) {
        try {
            val algorithm: Algorithm =
                Algorithm.HMAC256(BuildConfig.SECRET_TOKEN_JWT)

            val verifier: JWTVerifier = com.auth0.jwt.JWT.require(algorithm)
                .withIssuer("auth0")
                .build() //Reusable verifier instance

            val jwt: DecodedJWT = verifier.verify(token)
            //Log.i("jwt", jwt.payload)

        } catch (exception: JWTVerificationException) {
            //Invalid signature/claims
            Log.i(TAG, "Assinatura inválida")
        }
    }

    private fun decodeToken(token1: String): Sessao {
        var sessao: Sessao = Sessao(null, null, null, null, null, null, "")
        try {
            Log.i("Passei", "Passei aqui")
            //val jwt = JWT.decode(token1)
            val jwt = com.auth0.android.jwt.JWT(token1)
            Log.i("Passei2", "Passei aqui")

            val claims =
                jwt.claims //Key is the Claim name
            sessao = Sessao(
                jwt.getClaim("cpf").asString(),
                jwt.getClaim("userName").asString(),
                jwt.getClaim("exp").asInt(),
                jwt.getClaim("userId").asString(),
                jwt.getClaim("iat").asInt(),
                jwt.getClaim("email").asString(),
                jwt.toString()
            )

        } catch (exception: JWTDecodeException) {
            //Invalid token
            Log.i(TAG, "Error: invalid token")
        }
        return sessao
    }

    private fun isTokenValid(): Boolean {
        Log.i("Testando data", "---------------------------")
        val created = Date(UserPreferences.iat.toLong() * 1000)
        Log.i(TAG, "created: $created")
        Log.i(TAG, "created: ${UserPreferences.iat}")
        val expired = Date(UserPreferences.exp.toLong() * 1000)
        Log.i(TAG, "expired: $expired")
        Log.i(TAG, "expired: ${UserPreferences.exp}")

        val tsCurrent = System.currentTimeMillis() / 1000
        val ts = tsCurrent.toString()
        val now = Date(tsCurrent * 1000)
        Log.i(TAG, "now: $now")
        Log.i(TAG, "now: $tsCurrent")


//        if (UserPreferences.username != "") {
        if (tsCurrent < UserPreferences.exp) {
            return true
        }
        return false

    }


    private fun getDateTime(s: String): Date? {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date = java.util.Date(UserPreferences.exp.toLong() * 1000)
        val res = sdf.format(date)
        return date
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getIMEINumber(): String? {
//        var IMEINumber = ""
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.READ_PHONE_STATE
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            val telephonyMgr =
//                getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            IMEINumber = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                telephonyMgr.imei
//            } else {
//                Log.i("error_token", "invalid token").toString()
//                // telephonyMgr.deviceId
//            }
//        }
//        return IMEINumber
        var teste = ""
        var imei = ""
        val telephonyManager =
            getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                if (telephonyManager != null) {
                    imei = try {
                        telephonyManager.imei
                        teste = getIMEINumber().toString()
                        Log.i("imei1", teste).toString()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Settings.Secure.getString(
                            this.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    }
                }
            } else {
                ActivityCompat.requestPermissions(
                    this@LoginActivity,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    1010
                )
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this@LoginActivity,
                    Manifest.permission.READ_PHONE_STATE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (telephonyManager != null) {
                    imei = telephonyManager.imei
                    Log.i("imei2", imei).toString()

                }
            } else {
                ActivityCompat.requestPermissions(
                    this@LoginActivity,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    1010
                )
            }
        }
        return imei
    }

}




