package com.example.musicplayer2

import androidx.lifecycle.LiveData
import androidx.room.*



@Dao
interface MusicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(musicList: ArrayList<Music>)


    @Query("SELECT * FROM music ORDER BY id ASC")
    fun getAllMusic(): LiveData<List<Music>>

    @Delete
    suspend fun deleted(music: Music)

}