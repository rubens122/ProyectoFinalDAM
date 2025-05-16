package com.example.deezerproyecto.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.deezerproyecto.R
import com.example.deezerproyecto.databinding.FragmentDetallePlaylistBinding
import com.example.deezerproyecto.models.Playlist

class DetallePlaylistFragment(private val playlist: Playlist) : Fragment() {

    private var _binding: FragmentDetallePlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallePlaylistBinding.inflate(inflater, container, false)

        // Mostrar datos de la playlist
        binding.nombrePlaylist.text = playlist.nombre
        binding.textoPrivacidad.text = if (playlist.esPrivada) "Privada" else "Pública"

        // Si tiene imagen, cargarla
        if (playlist.rutaFoto.isNotEmpty()) {
            binding.imagenPlaylist.setImageURI(android.net.Uri.parse(playlist.rutaFoto))
        }

        // Listener para editar
        binding.botonEditar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, EditarPlaylistFragment(playlist))
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
