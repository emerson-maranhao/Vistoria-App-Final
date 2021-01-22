package br.gov.am.detran.appvistoria.presentation.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.gov.am.detran.appvistoria.dao.SurveyDao
import br.gov.am.detran.appvistoria.domain.Survey
import br.gov.am.detran.appvistoria.repository.SurveysRepository
import br.gov.am.detran.appvistoria.until.AppResult
import br.gov.am.detran.appvistoria.until.SingleLiveEvent
import kotlinx.coroutines.launch


class HomeViewModel(
    private val repository: SurveysRepository,
    private val context: Context,
    private val dao: SurveyDao
) : ViewModel() {
    private val mutableShowShimmer = MediatorLiveData<Boolean>()
    val showShimmer: MediatorLiveData<Boolean> = mutableShowShimmer
    val surveyList = MutableLiveData<List<Survey>>()
    val showError = SingleLiveEvent<String>()

    fun getSurveys() = viewModelScope.launch {

        when (val result = repository.getSurveys()) {
            is AppResult.Success -> {
                Log.i("result success:", result.successData.toString())
                surveyList.value = result.successData
                mutableShowShimmer.postValue(false)
                showError.value = null
            }
            is AppResult.Error -> showError.value = result.exception.message

        }
    }
}

