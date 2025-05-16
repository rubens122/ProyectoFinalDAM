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
import com.example.deezerproyecto.databinding.FragmentEditarPlaylistBinding
import com.example.deezerproyecto.models.Playlist
import com.google.firebase.database.FirebaseDatabase

class EditarPlaylistFragment(private val playlist: Playlist) : Fragment() {

    private var _binding: FragmentEditarPlaylistBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    // Nueva forma de recoger la imagen sin onActivityResult
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            binding.imagenPlaylist.setImageURI(imageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarPlaylistBinding.inflate(inflater, container, false)

        // Inicialización de los campos
        binding.campoNombrePlaylist.setText(playlist.nombre)
        binding.switchPrivacidad.isChecked = playlist.esPrivada
        if (playlist.rutaFoto.isNotEmpty()) {
            binding.imagenPlaylist.setImageURI(Uri.parse(playlist.rutaFoto))
        }

        // Selección de imagen
        binding.imagenPlaylist.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        // Guardar cambios y actualizar en Firebase
        binding.botonGuardar.setOnClickListener {
            val nuevoNombre = binding.campoNombrePlaylist.text.toString()
            val nuevaPrivacidad = binding.switchPrivacidad.isChecked

            if (nuevoNombre.isNotEmpty()) {
                // Actualizar los datos en el objeto
                playlist.nombre = nuevoNombre
                playlist.esPrivada = nuevaPrivacidad
                playlist.rutaFoto = imageUri?.toString() ?: playlist.rutaFoto

                // Actualización en Firebase
                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("playlists")

                reference.child(playlist.nombre).setValue(playlist)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Playlist actualizada", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
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
