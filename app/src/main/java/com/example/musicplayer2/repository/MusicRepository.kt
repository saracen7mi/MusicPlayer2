package com.example.musicplayer2

import android.content.Context
import androidx.lifecycle.LiveData



import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MusicRepository {

    companion object {
        private var musicDatabase: MusicDatabase? = null

        private fun initDataBase(context: Context): MusicDatabase? =
            MusicDatabase.getInstance(context)

        fun insert(context: Context, musicList: ArrayList<Music>) {
            musicDatabase = initDataBase(context)
            CoroutineScope(IO).launch {
                musicDatabase?.getMusicDao()?.insert(musicList)
            }
        }


        fun getAllMusic(context: Context): LiveData<List<Music>>? {
            musicDatabase = initDataBase(context)
            return musicDatabase?.getMusicDao()?.getAllMusic()
        }

        suspend fun deleted(music: Music) {
            musicDatabase!!.getMusicDao().deleted(music)
        }

    }
}