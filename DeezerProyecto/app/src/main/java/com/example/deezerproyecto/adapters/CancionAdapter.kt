package com.example.deezerproyecto.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.deezerproyecto.R
import com.example.deezerproyecto.databinding.ItemCancionBinding
import com.example.deezerproyecto.models.Track
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.squareup.picasso.Picasso

class CancionAdapter(
    private val canciones: MutableList<Track>
) : RecyclerView.Adapter<CancionAdapter.CancionViewHolder>() {

    private var currentPlayingPosition: Int = -1
    private var exoPlayer: ExoPlayer? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CancionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cancion, parent, false)
        return CancionViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: CancionViewHolder, position: Int) {
        holder.bind(canciones[position], position)
    }

    override fun getItemCount(): Int = canciones.size

    fun actualizarCanciones(nuevasCanciones: List<Track>) {
        canciones.clear()
        canciones.addAll(nuevasCanciones)
        notifyDataSetChanged()
    }

    inner class CancionViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCancionBinding.bind(itemView)
        private val playPauseButton: ImageButton = binding.playPauseButton

        fun bind(track: Track, position: Int) {
            binding.tituloCancion.text = track.title
            binding.artistaCancion.text = track.artist.name

            Picasso.get()
                .load(track.album.cover)
                .into(binding.imagenAlbum)

            // Establecer el icono del botón
            if (position == currentPlayingPosition) {
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
            } else {
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            }

            // Configurar el click del botón
            playPauseButton.setOnClickListener {
                if (position == currentPlayingPosition) {
                    // Pausar la reproducción
                    exoPlayer?.pause()
                    playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                    currentPlayingPosition = -1
                } else {
                    // Detener el anterior
                    if (currentPlayingPosition != -1) {
                        notifyItemChanged(currentPlayingPosition)
                    }
                    // Iniciar el nuevo
                    currentPlayingPosition = position
                    iniciarReproduccion(track.preview, context)
                    playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
                    notifyItemChanged(position)
                }
            }
        }
    }

    private fun iniciarReproduccion(url: String, context: Context) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
        exoPlayer?.stop()
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.prepare()
        exoPlayer?.play()
    }
}
