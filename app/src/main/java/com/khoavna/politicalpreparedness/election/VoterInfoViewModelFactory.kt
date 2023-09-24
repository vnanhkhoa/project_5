package com.khoavna.politicalpreparedness.election

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khoavna.politicalpreparedness.data.repository.local.LocalRepository
import com.khoavna.politicalpreparedness.data.repository.local.LocalRepositoryImpl
import com.khoavna.politicalpreparedness.data.repository.remote.VoterInfoRepository
import com.khoavna.politicalpreparedness.data.repository.remote.VoterInfoRepositoryImpl
import com.khoavna.politicalpreparedness.database.ElectionDatabase

class VoterInfoViewModelFactory(
    private val localRepository: LocalRepository,
    private val voterInfoRepository: VoterInfoRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(localRepository, voterInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

    companion object {
        fun getInstance(context: Context): VoterInfoViewModelFactory {
            val electionDatabase = ElectionDatabase.getInstance(context)
            return VoterInfoViewModelFactory(
                LocalRepositoryImpl(electionDao = electionDatabase.electionDao),
                VoterInfoRepositoryImpl()
            )
        }
    }
}