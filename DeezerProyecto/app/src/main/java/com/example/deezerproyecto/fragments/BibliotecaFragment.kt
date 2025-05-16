package com.example.deezerproyecto.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.deezerproyecto.R
import com.example.deezerproyecto.adapters.PlaylistAdapter
import com.example.deezerproyecto.databinding.FragmentBibliotecaBinding
import com.example.deezerproyecto.models.Playlist
import com.google.firebase.database.*

class BibliotecaFragment : Fragment() {

    private var _binding: FragmentBibliotecaBinding? = null
    private val binding get() = _binding!!
    private val playlists = mutableListOf<Playlist>()
    private lateinit var adapter: PlaylistAdapter
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBibliotecaBinding.inflate(inflater, container, false)

        // Inicialización del RecyclerView
        adapter = PlaylistAdapter(playlists) { playlist ->
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, DetallePlaylistFragment(playlist))
                .addToBackStack(null)
                .commit()
        }
        binding.recyclerPlaylists.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPlaylists.adapter = adapter

        // Inicializar Firebase
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("playlists")

        // Cargar las playlists
        cargarPlaylists()

        // Botón para añadir nueva Playlist
        binding.fabAddPlaylist.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.contenedorFragment, PlaylistFragment())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun cargarPlaylists() {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                playlists.clear()
                for (playlistSnapshot in snapshot.children) {
                    val playlist = playlistSnapshot.getValue(Playlist::class.java)
                    playlist?.let {
                        playlists.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Log o Toast para mostrar el error
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
