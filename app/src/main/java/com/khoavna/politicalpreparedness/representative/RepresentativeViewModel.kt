package com.khoavna.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.khoavna.politicalpreparedness.base.Result.Companion.onError
import com.khoavna.politicalpreparedness.base.Result.Companion.onSuccess
import com.khoavna.politicalpreparedness.data.repository.remote.RepresentativesRepository
import com.khoavna.politicalpreparedness.network.models.Address
import com.khoavna.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RepresentativeViewModel(
    private val representativesRepository: RepresentativesRepository
) : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>> = _representatives

    private val _address = MutableStateFlow(Address())
    val address: LiveData<Address> = _address.asLiveData()


    fun fetchRepresentatives(address: String){
        viewModelScope.launch {
            representativesRepository.fetch(address).apply {
                onSuccess {
                    val (offices, officials) = it
                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }
                }

                onError {
                    Log.d("RepresentativeViewModel", "fetchRepresentatives: $it")
                }
            }

        }
    }

    fun updateRepresentatives(tmp: List<Representative>) {
        _representatives.value = tmp
    }

    fun updateState(state: String) {
        _address.update {
            it.copy(state = state)
        }
    }

    fun updateAddress(address: Address) {
        _address.update {
            address
        }
    }

}
