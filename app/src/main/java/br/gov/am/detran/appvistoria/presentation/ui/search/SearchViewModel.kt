package br.gov.am.detran.appvistoria.presentation.ui.search

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.gov.am.detran.appvistoria.domain.VehicleRequest
import br.gov.am.detran.appvistoria.domain.Resource
import br.gov.am.detran.appvistoria.repository.SurveysRepository
import br.gov.am.detran.appvistoria.until.NetworkManager.isOnline

import kotlinx.coroutines.Dispatchers


class SearchViewModel(private val repository: SurveysRepository, private val context: Context) : ViewModel() {

    fun getVehicle(vehicle: VehicleRequest?) = liveData(Dispatchers.IO) {
        Log.i("body", vehicle.toString())
        emit(Resource.loading(data = null))
        if (isOnline(context)){
            Log.i("Emit", "Getting Repo...")
            try {
                Log.i("entrou no try", "Get Repository!")
                emit(Resource.success(data = repository.getVehicle(vehicle!!)))
                Log.i("Emit", "Get Repository!")

            } catch (exception: Exception) {
                Log.i("error", exception.toString())
                emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
            }
        }else{
            emit(Resource.error(data = null, message = "Verifique sua conexão!"))
        }

    }


}
