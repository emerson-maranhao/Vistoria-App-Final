package br.gov.am.detran.appvistoria.presentation.ui.add


import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import br.gov.am.detran.appvistoria.domain.RequestBodySurvey
import br.gov.am.detran.appvistoria.domain.Resource
import br.gov.am.detran.appvistoria.repository.SurveysRepository
import kotlinx.coroutines.Dispatchers

class AddSurveyViewModel(private val repository: SurveysRepository) : ViewModel() {

    //this is the data that we will fetch asynchronously
        val photoUrl = MutableLiveData<Bitmap>()



    fun insert(requestBodySurvey: RequestBodySurvey) = liveData(Dispatchers.Main) {
        emit(Resource.loading(data = null))
        Log.i("Emit", "Sending Survey...")
        try {
            emit(Resource.success(data = repository.setSurvey(requestBodySurvey)))
            Log.i("Emit", "Survey Send!")

        } catch (exception: Exception) {
            Log.i("error", exception.toString())
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        photoUrl.postValue(bitmap)
    }

    fun clear() {
        photoUrl.value = null
    }
}
