package com.khoavna.politicalpreparedness.election

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.khoavna.politicalpreparedness.data.repository.local.LocalRepository
import com.khoavna.politicalpreparedness.data.repository.local.LocalRepositoryImpl
import com.khoavna.politicalpreparedness.data.repository.remote.ElectionRepository
import com.khoavna.politicalpreparedness.data.repository.remote.ElectionRepositoryImpl
import com.khoavna.politicalpreparedness.database.ElectionDatabase

class ElectionsViewModelFactory(
    private val localRepository: LocalRepository,
    private val electionRepository: ElectionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(localRepository, electionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }

    companion object {
        fun getInstance(context: Context): ElectionsViewModelFactory {
            val electionDatabase = ElectionDatabase.getInstance(context)
            return ElectionsViewModelFactory(
                LocalRepositoryImpl(electionDao = electionDatabase.electionDao),
                ElectionRepositoryImpl()
            )
        }
    }
}