package com.puntogris.areyouarobot.data.local

import androidx.room.*

@Dao
interface PlayerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Player)

    @Update
    suspend fun update(product: Player)

    @Query("DELETE FROM player_table")
    suspend fun clear()

    @Query("SELECT * from player_table WHERE id = :key")
    suspend fun get(key: String): Player?


}