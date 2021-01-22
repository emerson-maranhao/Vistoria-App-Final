package br.gov.am.detran.appvistoria.until

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import br.gov.am.detran.appvistoria.R
import okhttp3.OkHttpClient
import java.security.SecureRandom
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

fun Context.noNetworkConnectivityError(): AppResult.Error {
    return AppResult.Error(Exception(this.resources.getString(R.string.no_network_connectivity)))
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        add(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

fun AppCompatActivity.replaceFragment(
    fragment: Fragment,
    frameId: Int,
    backStackTag: String? = null,
) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun OkHttpClient.Builder.ignoreAllSSLErrors(): OkHttpClient.Builder {
    val naiveTrustManager = object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<out java.security.cert.X509Certificate>? =
            arrayOf()

        override fun checkClientTrusted(
            p0: Array<out java.security.cert.X509Certificate>?,
            authType: String?,
        ) = Unit

        override fun checkServerTrusted(
            p0: Array<out java.security.cert.X509Certificate>?,
            authType: String?,
        ) = Unit
    }

    val insecureSocketFactory = SSLContext.getInstance("TLSv1.2").apply {
        val trustAllCerts = arrayOf<TrustManager>(naiveTrustManager)
        init(null, trustAllCerts, SecureRandom())
    }.socketFactory

    sslSocketFactory(insecureSocketFactory, naiveTrustManager)
    hostnameVerifier(HostnameVerifier { _, _ -> true })
    return this
}