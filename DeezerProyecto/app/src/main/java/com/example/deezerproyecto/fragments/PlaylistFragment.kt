package com.example.deezerproyecto.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.deezerproyecto.databinding.FragmentPlaylistBinding
import com.example.deezerproyecto.models.Playlist
import com.google.firebase.database.FirebaseDatabase

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private lateinit var database: FirebaseDatabase

    // Nueva forma de recoger la imagen sin onActivityResult
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            binding.imagenPreview.setImageURI(imageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance()

        binding.botonSeleccionarImagen.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.botonCrearPlaylist.setOnClickListener {
            val nombre = binding.campoNombrePlaylist.text.toString()
            val esPrivada = binding.switchPrivacidad.isChecked
            val rutaFoto = imageUri?.toString() ?: ""

            if (nombre.isNotEmpty()) {
                val nuevaPlaylist = Playlist(nombre, mutableListOf(), rutaFoto, esPrivada)

                // Guardar en Firebase
                database.getReference("playlists").child(nombre).setValue(nuevaPlaylist)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Playlist creada correctamente", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al crear la playlist", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(requireContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
