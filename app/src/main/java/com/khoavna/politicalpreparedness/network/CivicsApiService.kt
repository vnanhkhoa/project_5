package com.khoavna.politicalpreparedness.network

import com.khoavna.politicalpreparedness.network.models.ElectionResponse
import com.khoavna.politicalpreparedness.network.models.RepresentativeResponse
import com.khoavna.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.khoavna.politicalpreparedness.network.base.ApiServiceImpl
import com.khoavna.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

private val moshi = Moshi.Builder()
    .add(ElectionAdapter())
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(CivicsHttpClient.getClient())
    .baseUrl(BASE_URL)
    .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {
    @GET("elections")
    fun fetchElections(): Call<ElectionResponse>

    @GET("voterinfo")
    fun fetchVoterInfo(
        @Query("address") address: String,
        @Query("electionId") electionId: Long,
        @Query("returnAllAvailableData") returnAllAvailableData: Boolean,
        @Query("productionDataOnly") productionDataOnly: Boolean,
    ): Call<VoterInfoResponse>

    @GET("representatives")
    fun fetchRepresentatives(@Query("address") address: String): Call<RepresentativeResponse>
}

object CivicsApi {
    private val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }

    fun fetchRepresentatives(address: String) =
        ApiServiceImpl(retrofitService.fetchRepresentatives(address))


    fun fetchElections() = ApiServiceImpl(retrofitService.fetchElections())

    fun fetchVoterInfo(
        address: String,
        electionId: Long,
        returnAllAvailableData: Boolean,
        productionDataOnly: Boolean
    ) = ApiServiceImpl(
        retrofitService.fetchVoterInfo(
            address,
            electionId,
            returnAllAvailableData,
            productionDataOnly
        )
    )

}