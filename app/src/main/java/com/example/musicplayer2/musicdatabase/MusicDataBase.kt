package com.example.musicplayer2


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase



@Database(entities = [Music::class], version = 2,exportSchema = false)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun getMusicDao(): MusicDao

    companion object {
        private const val MUSIC_NAME = "music"

        @Volatile
        private var instance: MusicDatabase? = null



        fun getInstance(context: Context): MusicDatabase? {
            if (instance == null) {
                synchronized(MusicDatabase::class.java)
                {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context, MusicDatabase::class.java,
                            MUSIC_NAME
                        )
                            .fallbackToDestructiveMigration()

                            .allowMainThreadQueries()

                            .build()
                    }
                }
            }

            return instance
        }
    }
}