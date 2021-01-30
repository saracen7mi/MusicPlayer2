package com.example.musicplayer2


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer2.R



import kotlinx.android.synthetic.main.activity_item.view.*


class mAdapter(
    private val context: Context,
    private var mList: ArrayList<Music>,
    private val listener: mListener,
    var onDelete: (Music) -> Unit
) : RecyclerView.Adapter<mAdapter.MusicViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder =
        MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
        )

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = mList[position]
        holder.songNameMusic.text = music.songNameMusic
        holder.artistNameMusic.text = music.artistNameMusic
        holder.timeMusic.text = music.timeMusic

        holder.delete.setOnClickListener { onDelete(music) }

    }

    override fun getItemCount(): Int = mList.size

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songNameMusic: TextView = itemView.findViewById(R.id.songNameMusic)
        val artistNameMusic: TextView = itemView.findViewById(R.id.artistNameMusic)
        val timeMusic: TextView = itemView.findViewById(R.id.timeMusic)
        val delete = itemView.delete
        val row = itemView.row

        init {
            itemView.setOnClickListener {

                listener.onClickListener(adapterPosition)

            }

        }
    }

    fun setMusicData(musicList: ArrayList<Music>) {
        this.mList = musicList
        notifyDataSetChanged()
    }
}