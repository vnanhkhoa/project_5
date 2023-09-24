package com.khoavna.politicalpreparedness.network.jsonadapter

import com.khoavna.politicalpreparedness.network.models.Division
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ElectionAdapter {
    @FromJson
    fun divisionFromJson(ocdDivisionId: String): Division {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val districtDelimiter = "district:"
        val country = ocdDivisionId.substringAfter(countryDelimiter, "")
            .substringBefore("/")
        val district = ocdDivisionId.substringAfter(districtDelimiter, "")
            .substringBefore("/")
        val state = ocdDivisionId.substringAfter(stateDelimiter, district)
            .substringBefore("/")
        return Division(ocdDivisionId, country, state)
    }

    @ToJson
    fun divisionToJson(division: Division): String {
        return division.id
    }
}