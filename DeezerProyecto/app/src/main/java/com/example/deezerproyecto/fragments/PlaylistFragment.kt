package com.example.deezerproyecto.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.deezerproyecto.R
import com.example.deezerproyecto.databinding.FragmentPlaylistBinding
import com.example.deezerproyecto.models.Playlist
import com.example.deezerproyecto.models.Track

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private val IMAGE_PICK_CODE = 1000

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        binding.botonSeleccionarImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        binding.botonCrearPlaylist.setOnClickListener {
            val nombre = binding.campoNombrePlaylist.text.toString()
            val esPrivada = binding.switchPrivacidad.isChecked
            val rutaFoto = imageUri?.path ?: ""

            if (nombre.isNotEmpty()) {
                val nuevaPlaylist = Playlist(nombre, mutableListOf<Track>(), rutaFoto, esPrivada)
                Toast.makeText(requireContext(), "Playlist creada: $nombre", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.imagenPreview.setImageURI(imageUri)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
