package com.khoavna.politicalpreparedness.network.models

data class ElectionRemote(
    val id: String,
    val name: String,
    val ocdDivisionId: String,
    val electionDay: String,
)