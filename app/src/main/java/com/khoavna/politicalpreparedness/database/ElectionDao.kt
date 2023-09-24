package com.khoavna.politicalpreparedness.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.khoavna.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    @Query("SELECT * FROM `election_table` WHERE `id` = :id")
    fun findById(id: String): Election?

    @Query("SELECT * FROM `election_table` ORDER BY `electionDay` DESC")
    fun getElections(): List<Election>

    @Query("DELETE FROM `election_table` WHERE `id` = :id")
    fun deleteById(id: String)

    @Query("DELETE FROM `election_table`")
    fun deleteAll()

}