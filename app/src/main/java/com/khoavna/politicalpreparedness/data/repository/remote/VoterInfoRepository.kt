package com.khoavna.politicalpreparedness.data.repository.remote

import com.khoavna.politicalpreparedness.base.Result
import com.khoavna.politicalpreparedness.network.CivicsApi
import com.khoavna.politicalpreparedness.network.base.ApiService
import com.khoavna.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VoterInfoRepositoryImpl(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : VoterInfoRepository {
    private var apiService: ApiService<VoterInfoResponse>? = null
    override suspend fun fetch(dataParam: DataParam): Result<VoterInfoResponse> =
        withContext(dispatcher) {
            try {
                apiService = CivicsApi.fetchVoterInfo(
                    address = dataParam.address,
                    electionId = dataParam.electionId,
                    returnAllAvailableData = dataParam.returnAllAvailableData,
                    productionDataOnly = dataParam.productionDataOnly
                )
                val result = apiService?.fetch() ?: throw Exception("API Error")
                Result.SUCCESS(result)
            } catch (e: Exception) {
                Result.ERROR(e.localizedMessage)
            }
        }

    override suspend fun cancel() {
        apiService?.cancel()
    }
}

data class DataParam(
    val address: String,
    val electionId: Long,
    val returnAllAvailableData: Boolean,
    val productionDataOnly: Boolean
)

interface VoterInfoRepository {
    suspend fun fetch(dataParam: DataParam): Result<VoterInfoResponse>
    suspend fun cancel()
}