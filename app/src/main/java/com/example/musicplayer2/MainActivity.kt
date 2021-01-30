package com.example.musicplayer2


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), mListener {

    private lateinit var recyclerView: RecyclerView
    lateinit var mList: ArrayList<Music>
    private lateinit var musicViewModel: MusicViewModel
    private val READ_STORAGE = 100
    private lateinit var mAdapter: mAdapter
    private lateinit var mediaPlayer: MediaPlayer

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer = MediaPlayer()
        mList = ArrayList()
        initRecyclerView()
        initViewModel()
        getPermission()

    }

    private fun initViewModel() {
        musicViewModel = ViewModelProvider(this).get(MusicViewModel::class.java)

        musicViewModel.getAllMusic(this)?.observe(this, androidx.lifecycle.Observer {
            mAdapter.apply {
                setMusicData(it as ArrayList<Music>)
                notifyDataSetChanged()
            }

        })


    }

    private fun initRecyclerView() {
        val onDelete: (Music) -> Unit = {
            musicViewModel!!.deleted(it)
        }
        recyclerView = findViewById(R.id.recyclerView)
        mAdapter = mAdapter(this, ArrayList<Music>(), this, onDelete)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = mAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getPermission() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE

            )

        } else {
            readExternalData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            READ_STORAGE -> if ((grantResults.isNotEmpty()) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                readExternalData()

            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)

    private fun readExternalData() {
        val contentResolver = contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val music: Music
                val songNameMusic =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val artistNameMusic =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val timeMusic =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val songUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                val secToMin = TimeUnit.MILLISECONDS.toMinutes(timeMusic)

                music = Music(songNameMusic, artistNameMusic, "$secToMin min", songUrl)
                mList.add(music)
                Log.d("main", "readExternalData: ${music.artistNameMusic} ")
            }

            musicViewModel.insert(this, mList)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClickListener(position: Int) {
        val db = Intent(this, MusicSong::class.java)
        db.putExtra("song", mList)
        db.putExtra("POSITION", position)
        startActivity(db)




    }


}