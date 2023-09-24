package com.khoavna.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoavna.politicalpreparedness.base.Result.Companion.onError
import com.khoavna.politicalpreparedness.base.Result.Companion.onSuccess
import com.khoavna.politicalpreparedness.data.repository.local.LocalRepository
import com.khoavna.politicalpreparedness.data.repository.remote.ElectionRepository
import com.khoavna.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.khoavna.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class ElectionsViewModel(
    private val localRepository: LocalRepository,
    private val electionRepository: ElectionRepository
): ViewModel() {

    private val _elections = MutableLiveData<List<Election>>()
    val elections: LiveData<List<Election>> = _elections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>> = _savedElections

    val showLoading = MutableLiveData<Boolean>()
    val showToast = MutableLiveData<String>()

    fun fetchAll() {
        getUpcomingElections()
        onSavedElections()
    }

    private fun onSavedElections() {
        viewModelScope.launch {
            localRepository.getElections().apply {
                onSuccess {
                    _savedElections.value = it
                }
            }
        }
    }

    private fun getUpcomingElections() {
        showLoading.value = true
        viewModelScope.launch {
            electionRepository.fetch().apply {
                onSuccess {
                    val electionAdapter = ElectionAdapter()
                    _elections.value = it.elections.map { election ->
                        Election(
                            id = election.id,
                            division = electionAdapter.divisionFromJson(election.ocdDivisionId),
                            electionDay = election.electionDay,
                            ocdDivisionId = election.ocdDivisionId,
                            name = election.name,
                        )
                    }
                    showLoading.value = false
                }

                onError {
                    showToast.value = it
                    showLoading.value = false
                }
            }
        }
    }
}