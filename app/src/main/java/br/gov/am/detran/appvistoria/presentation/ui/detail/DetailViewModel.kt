package br.gov.am.detran.appvistoria.presentation.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.gov.am.detran.appvistoria.domain.Survey

class DetailViewModel: ViewModel() {

    //this is the data that we will fetch asynchronously
    val surveyLiveData: MutableLiveData<List<Survey>> = MutableLiveData()

    //we will call this method to get the data
    fun getDetails() { //if the list is null
//        ApiService.service.getSurveys().enqueue(
//            object : Callback<List<Survey>> {
//
//                override fun onResponse(
//                    call: Call<List<Survey>?>,
//                    response: Response<List<Survey>?>
//                ) {
//                    if (response.isSuccessful) {
//
//                        val surveys: MutableList<Survey> = mutableListOf()
//
//
//                        response.body()?.let {
//                            val s: List<Survey> = it
//                            //surveyLiveData.value = it
//                            surveyLiveData.value = s
//                            Log.i("ok","ok------------------------->"+ surveyLiveData)
//
//                        }
//
//
//                    }
//                }
//
//                override fun onFailure(call: Call<List<Survey>>, t: Throwable) {
//                    Log.i("erro","erro------------------------->"+t)
//                }
//
//            })


    }

}