package com.puntogris.areyouarobot.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_table")
data class Player(

    @PrimaryKey
    val id: String = "vacio",

    @ColumnInfo(name = "name")
    val name: String = "Vacio",

    @ColumnInfo(name = "max_score")
    val maxScore: Int = 0

)
