package com.example.deezerproyecto.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezerproyecto.R
import com.example.deezerproyecto.databinding.FragmentListaPlaylistsBinding
import com.example.deezerproyecto.models.Playlist
import com.example.deezerproyecto.adapters.PlaylistAdapter

class ListaPlaylistsFragment : Fragment() {

    private var _binding: FragmentListaPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlists = mutableListOf<Playlist>()
    private lateinit var adapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaPlaylistsBinding.inflate(inflater, container, false)

        // Configuración del RecyclerView
        adapter = PlaylistAdapter(playlists) { playlist ->
            // Acción al hacer click, ejemplo, navegar al detalle
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, DetallePlaylistFragment.newInstance(playlist))
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerViewPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPlaylists.adapter = adapter

        // Cargar playlists (aquí podrías cargar de una base de datos local o de un servicio)
        cargarPlaylists()

        return binding.root
    }

    private fun cargarPlaylists() {
        // Aquí deberías cargar las playlists que hayas creado
        playlists.add(Playlist("Mi Playlist 1", mutableListOf(), "", true))
        playlists.add(Playlist("Mi Playlist 2", mutableListOf(), "", false))
        playlists.add(Playlist("Mi Playlist 3", mutableListOf(), "", true))

        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
