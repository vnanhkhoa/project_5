package com.khoavna.politicalpreparedness.data.repository.local

import com.khoavna.politicalpreparedness.base.Result
import com.khoavna.politicalpreparedness.network.models.Election

interface LocalRepository {
    suspend fun insertElection(election: Election)
    suspend fun findElectionById(id: String): Result<Election>
    suspend fun getElections():Result<List<Election>>
    suspend fun deleteById(id: String)
    suspend fun deleteAll()
}