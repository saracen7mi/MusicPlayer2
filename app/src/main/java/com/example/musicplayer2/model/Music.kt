package com.example.musicplayer2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "music")

data class Music(
    @ColumnInfo(name = "songNameMusic")
    val songNameMusic: String?,
    @ColumnInfo(name = "artistNameMusic")
    val artistNameMusic: String?,
    @ColumnInfo(name = "timeMusic")
    val timeMusic: String?,
    @ColumnInfo(name = "url")
    val url: String?
) : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}