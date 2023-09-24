package com.khoavna.politicalpreparedness.election

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khoavna.politicalpreparedness.R
import com.khoavna.politicalpreparedness.base.Result.Companion.onError
import com.khoavna.politicalpreparedness.base.Result.Companion.onSuccess
import com.khoavna.politicalpreparedness.data.repository.local.LocalRepository
import com.khoavna.politicalpreparedness.data.repository.remote.DataParam
import com.khoavna.politicalpreparedness.data.repository.remote.VoterInfoRepository
import com.khoavna.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.khoavna.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

class VoterInfoViewModel(
    private val localRepository: LocalRepository,
    private val voterInfoRepository: VoterInfoRepository
) : ViewModel() {

    val election = MutableLiveData<Election>()
    val textBtn = MutableLiveData(R.string.follow)
    val address = MutableLiveData("")
    val addressVisibility = MutableLiveData(View.VISIBLE)
    val loadingVisibility = MutableLiveData(View.GONE)
    val showToast = MutableLiveData<String>()
    lateinit var voteLink: String
        private set
    lateinit var ballotLink: String
        private set
    var isFollow = false
        private set

    fun getVoterInfo(id: String, divisionId: String) {
        val electionAdapter = ElectionAdapter()
        loadingVisibility.value = View.VISIBLE
        viewModelScope.launch {
            val dataParam = DataParam(
                electionId = id.toLong(),
                address = divisionId,
                productionDataOnly = true,
                returnAllAvailableData = true,
            )
            voterInfoRepository.fetch(dataParam).apply {
                onSuccess {
                    election.value = Election(
                        id = it.election.id,
                        name = it.election.name,
                        electionDay = it.election.electionDay,
                        ocdDivisionId = it.election.ocdDivisionId,
                        division = electionAdapter.divisionFromJson(it.election.ocdDivisionId)
                    )

                    if (!it.state.isNullOrEmpty()) {
                        it.state.first().electionAdministrationBody.run {
                            address.value = correspondenceAddress?.line1 ?: ""
                            voteLink = votingLocationFinderUrl ?: ""
                            ballotLink = ballotInfoUrl ?: ""
                        }
                    } else {
                        addressVisibility.value = View.GONE
                    }
                    loadingVisibility.value = View.GONE
                }

                onError {
                    showToast.value = it
                    loadingVisibility.value = View.GONE
                }
            }
        }
    }

    fun saveElection() {
        viewModelScope.launch {
            election.value?.let {
                localRepository.insertElection(it)
            }
        }
    }

    fun deleteElection() {
        viewModelScope.launch {
            election.value?.let {
                localRepository.deleteById(it.id)
            }
        }
    }

    fun getElection(id: String) {
        viewModelScope.launch {
            localRepository.findElectionById(id).apply {
                onSuccess {
                    election.value = it
                    isFollow = true
                    textBtn.value = R.string.unfollow
                }

                onError {
                    isFollow = false
                    textBtn.value = R.string.follow
                }
            }
        }
    }
}