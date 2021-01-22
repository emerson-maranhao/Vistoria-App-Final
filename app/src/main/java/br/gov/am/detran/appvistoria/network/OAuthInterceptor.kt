package br.gov.am.detran.appvistoria.network

import android.util.Log
import br.gov.am.detran.appvistoria.session.UserPreferences

import okhttp3.Interceptor
import okhttp3.Request

class OAuthInterceptor(private var tokenType: String, private var acceessToken: String) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        tokenType="Bearer"
        acceessToken= UserPreferences.token
        Log.i("OAuth TOken", acceessToken)

        Log.i("Interceptor Token:",UserPreferences.token)

        var request = chain.request()
        request = request.newBuilder().header("Authorization", "$tokenType $acceessToken").build()
        Log.i("acceessToken", acceessToken)
        val builder = request.newBuilder()
        var response = chain.proceed(request)
        Log.i("OAuth Cod Response", response.code.toString())
        if (response.code == 401) {
            Log.i("errorrrrr","eeeeeeerrrrrrrrrrrrr")
            Log.i("acceessToken", acceessToken.toString())

            synchronized(chain) {
                //perform all 401 in sync blocks, to avoid multiply token updates
                val currentToken: String = acceessToken //get currently stored token
                if (currentToken != null && currentToken == acceessToken) { //compare current token with token that was stored before, if it was not updated - do update
                    val code: Int = refreshToken() / 100 //refresh token
                    if (code != 2) { //if refresh token failed for some reason
                        if (code == 4) //only if response is 400, 500 might mean that token was not updated
                            logout() //go to login screen
                        return response //if token refresh failed - show error to user
                    }
                }
                if (acceessToken != null) { //retry requires new auth token,
                    setAuthHeader(builder, acceessToken) //set auth token to updated
                    request = builder.build()
                    return chain.proceed(request) //repeat request with new token
                }
            }

        }
        if (response.code==400) {

            Log.i("Erro 400", response.message)
        }else
         {
             //Log.i("OAuthInterceptor", "error")
            Log.i("OAuth Response", response.message)
        }

        return response

    }

    private fun setAuthHeader(builder: Request.Builder, token: String?) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header("Authorization", String.format("Bearer %s", token))
    }

    private fun refreshToken(): Int {
        //Refresh token, synchronously, save it, and return result code
        //you might use retrofit here
        return 2
    }

    private fun logout(): Int {
        //logout your user
        return 1
    }
}