package br.gov.am.detran.appvistoria.presentation.ui.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.gov.am.detran.appvistoria.domain.User
import br.gov.am.detran.appvistoria.domain.Resource
import br.gov.am.detran.appvistoria.repository.SurveysRepository
import br.gov.am.detran.appvistoria.until.NetworkManager.isOnline
import kotlinx.coroutines.Dispatchers


class LoginViewModel(
    private val repository: SurveysRepository, private val context: Context
) : ViewModel() {

    fun getLogin(user: User?) = liveData(Dispatchers.Main) {
        if (isOnline(context)){
            Log.i("body", user.toString())
            emit(Resource.loading(data = null))

            try {
                emit(Resource.success(data = repository.getLogin(user!!)))
                Log.i("Emit", "Get Repository!")

            } catch (exception: Exception) {
                Log.i("error", exception.toString())
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }
        else{
            emit(Resource.error(data = null, message = "Verifique sua conexão e tente novamente!"))

        }

    }


//    fun getLogin() {
//        loginLiveData.value = createFakeLogin()[1]
//    }
//
//    fun createFakeLogin(): List<User> {
//        return listOf(
//            return listOf(
//                User(
//                    "11111111111",
//                    "123456",
//                    "Usuario1"
//                ),
//                User(
//                    "22222222222",
//                    "123456",
//                    "Usuario2"
//
//                ),
//                User(
//                    "33333333333",
//                    "123456",
//                    "Usuario3"
//
//                )
//            )
//        )
//
//
//    }
}


