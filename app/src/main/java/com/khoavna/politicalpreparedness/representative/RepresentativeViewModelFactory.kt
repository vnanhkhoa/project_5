package com.khoavna.politicalpreparedness.representative

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khoavna.politicalpreparedness.data.repository.local.LocalRepository
import com.khoavna.politicalpreparedness.data.repository.local.LocalRepositoryImpl
import com.khoavna.politicalpreparedness.data.repository.remote.RepresentativesRepository
import com.khoavna.politicalpreparedness.data.repository.remote.RepresentativesRepositoryImpl
import com.khoavna.politicalpreparedness.data.repository.remote.VoterInfoRepository
import com.khoavna.politicalpreparedness.data.repository.remote.VoterInfoRepositoryImpl
import com.khoavna.politicalpreparedness.database.ElectionDatabase

class RepresentativeViewModelFactory(
    private val representativesRepository: RepresentativesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RepresentativeViewModel::class.java)) {
            return RepresentativeViewModel(representativesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

    companion object {
        fun getInstance(): RepresentativeViewModelFactory {
            return RepresentativeViewModelFactory(
                RepresentativesRepositoryImpl()
            )
        }
    }
}