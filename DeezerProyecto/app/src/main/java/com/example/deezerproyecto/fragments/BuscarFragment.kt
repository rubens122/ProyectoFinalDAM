package com.example.deezerproyecto.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deezerproyecto.R
import com.example.deezerproyecto.adapters.CancionAdapter
import com.example.deezerproyecto.api.DeezerClient
import com.example.deezerproyecto.api.DeezerService
import com.example.deezerproyecto.models.Track
import com.example.deezerproyecto.models.TrackResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuscarFragment : Fragment() {

    private lateinit var campoBusqueda: EditText
    private lateinit var botonBuscar: Button
    private lateinit var recyclerViewCanciones: RecyclerView
    private lateinit var adapter: CancionAdapter

    private val deezerService: DeezerService = DeezerClient.retrofit.create(DeezerService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buscar, container, false)

        campoBusqueda = view.findViewById(R.id.campoBusqueda)
        botonBuscar = view.findViewById(R.id.botonBuscar)
        recyclerViewCanciones = view.findViewById(R.id.recyclerCanciones)


        adapter = CancionAdapter(mutableListOf())

        recyclerViewCanciones.layoutManager = LinearLayoutManager(context)
        recyclerViewCanciones.adapter = adapter

        botonBuscar.setOnClickListener {
            val query = campoBusqueda.text.toString()
            if (query.isNotEmpty()) {
                buscarCanciones(query)
            }
        }

        return view
    }

    private fun buscarCanciones(query: String) {
        val call = deezerService.buscarCancion(query)
        call.enqueue(object : Callback<TrackResponse> {
            override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {
                if (response.isSuccessful) {
                    val canciones = response.body()?.data ?: emptyList()
                    actualizarCanciones(canciones)
                } else {
                    Log.e("BuscarFragment", "Error en la respuesta: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                Log.e("BuscarFragment", "Error en la petición: ${t.message}")
            }
        })
    }

    private fun actualizarCanciones(canciones: List<Track>) {
        adapter.actualizarCanciones(canciones)
    }
}
