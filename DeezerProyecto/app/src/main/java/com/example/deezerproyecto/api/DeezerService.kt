package com.example.deezerproyecto.api

import com.example.deezerproyecto.models.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerService {
    @GET("search")
    fun buscarCancion(
        @Query("q") query: String
    ): Call<TrackResponse>
}