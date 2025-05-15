package com.example.deezerproyecto.models

data class Playlist(
    val nombre: String,
    val canciones: MutableList<Track>,
    val rutaFoto: String,
    val esPrivada: Boolean
)