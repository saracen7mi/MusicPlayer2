package com.example.musicplayer2

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel



import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MusicViewModel() : ViewModel() {

    fun insert(context: Context, musicList: ArrayList<Music>) {
        MusicRepository.insert(context, musicList)
    }
    fun getAllMusic(context: Context): LiveData<List<Music>>? = MusicRepository.getAllMusic(context)
    fun deleted(music: Music) = CoroutineScope(Dispatchers.Main).launch {
        MusicRepository.deleted(music)

    }

}