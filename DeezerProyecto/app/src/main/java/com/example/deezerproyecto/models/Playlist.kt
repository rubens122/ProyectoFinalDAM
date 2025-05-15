package com.example.deezerproyecto.models

data class Playlist(
    val nombre: String,
    val canciones: MutableList<Track>,
    val fotoRuta: String,    // Ruta de la imagen en el almacenamiento
    val esPrivada: Boolean   // Estado de privacidad
)
