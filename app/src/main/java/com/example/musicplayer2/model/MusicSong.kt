package com.example.musicplayer2


import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_play_song.*


class MusicSong : AppCompatActivity(), View.OnClickListener {

    var listMusic = ArrayList<Music>()
    private var position: Int = 0
    private lateinit var mPlayer: MediaPlayer
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var STEP_VALUE = 4000
    private lateinit var musicViewModel: MusicViewModel
    lateinit var uri: Uri
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)
        handler = Handler()
        mPlayer = MediaPlayer()
        musicViewModel = MusicViewModel()
        listMusic = ArrayList<Music>()

        getSerializableData()

        loadData()
        initSeekBar()
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mPlayer.seekTo(progress * 1000)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


    }


    private fun initSeekBar() {
        seekbar.max = mPlayer.seconds
        runnable = Runnable {
            seekbar.progress = mPlayer.currentSeconds
            val diff = mPlayer.seconds - mPlayer.currentSeconds
            handler.postDelayed(runnable, 1000)
        }
        handler.postDelayed(runnable, 1000)
    }

    private val MediaPlayer.seconds: Int
        get() {
            return this.duration / 1000
        }

    private val MediaPlayer.currentSeconds: Int
        get() {
            return this.currentPosition / 1000
        }

    private fun loadData() {


        songName.text = listMusic[position].songNameMusic
        artistName.text = listMusic[position].artistNameMusic
        try {

            mPlayer.setDataSource(listMusic[position].url)
            mPlayer.prepare()

            mPlayer.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }
        previous.setOnClickListener(this)
        next.setOnClickListener(this)
        play.setOnClickListener(this)
    }


    fun getSerializableData() {

        val bundle: Bundle? = intent.extras
        listMusic = bundle?.getSerializable("song") as ArrayList<Music>
        position = bundle.getInt("POSITION", -1)


    }

    override fun onClick(v: View?) {


        when (v?.id) {
            R.id.previous -> {

                if (mPlayer.isPlaying) {
                    mPlayer.stop()
                    mPlayer.release()


                    position = (position - 1) % listMusic.size
                    uri = Uri.parse(listMusic.get(position).url)

                    mPlayer = MediaPlayer.create(this, uri)
                    songName.text = listMusic[position].songNameMusic
                    artistName.text = listMusic[position].artistNameMusic

                    mPlayer.start()
                } else {
                    initSeekBar()
                    play.setImageResource(R.drawable.pause)

                    mPlayer.start()

                }


                var seekto = mPlayer.getCurrentPosition() - STEP_VALUE;
                if (seekto > mPlayer.getDuration())
                    seekto = mPlayer.getDuration()
                mPlayer.pause()
                mPlayer.seekTo(seekto)
                mPlayer.start()


            }
            R.id.play -> {
                if (mPlayer.isPlaying) {
                    play.setImageResource(R.drawable.play)
                    mPlayer.seekTo(position)
                    mPlayer.pause()
                    handler.removeCallbacks(runnable)

                } else {
                    initSeekBar()
                    play.setImageResource(R.drawable.pause)

                    mPlayer.start()

                }
            }
            R.id.next -> {
                if (mPlayer.isPlaying) {

                    mPlayer.stop()
                    mPlayer.release()


                    position = (position + 1) % listMusic.size
                    uri = Uri.parse(listMusic.get(position).url)

                    mPlayer = MediaPlayer.create(this, uri)
                    songName.text = listMusic[position].songNameMusic
                    artistName.text = listMusic[position].artistNameMusic

                    mPlayer.start()
                } else {
                    initSeekBar()
                    play.setImageResource(R.drawable.pause)

                    mPlayer.start()

                }


                var seekto = mPlayer.getCurrentPosition() + STEP_VALUE;
                if (seekto > mPlayer.getDuration())
                    seekto = mPlayer.getDuration()
                mPlayer.pause()
                mPlayer.seekTo(seekto)

                mPlayer.start()

            }

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer.stop()

    }


}