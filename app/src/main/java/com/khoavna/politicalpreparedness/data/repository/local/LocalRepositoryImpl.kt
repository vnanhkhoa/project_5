package com.khoavna.politicalpreparedness.data.repository.local

import com.khoavna.politicalpreparedness.base.Result
import com.khoavna.politicalpreparedness.database.ElectionDao
import com.khoavna.politicalpreparedness.network.models.Election
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRepositoryImpl(
    private val electionDao: ElectionDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : LocalRepository {
    override suspend fun insertElection(election: Election) = withContext(dispatcher) {
        electionDao.insert(election)
    }

    override suspend fun findElectionById(id: String): Result<Election> = withContext(dispatcher) {
        return@withContext try {
            val election = electionDao.findById(id) ?: throw Exception("Election not found")
            Result.SUCCESS(election)
        } catch (ex: Exception) {
            Result.ERROR(ex.localizedMessage)
        }
    }

    override suspend fun getElections(): Result<List<Election>> = withContext(dispatcher) {
        try {
            val elections = electionDao.getElections()
            if (elections.isEmpty()) throw Exception("Elections empty")
            return@withContext Result.SUCCESS(elections)
        } catch (ex: Exception) {
            return@withContext Result.ERROR(ex.localizedMessage)
        }
    }

    override suspend fun deleteById(id: String) = withContext(dispatcher) {
        electionDao.deleteById(id)
    }

    override suspend fun deleteAll() = withContext(dispatcher) {
        electionDao.deleteAll()
    }
}