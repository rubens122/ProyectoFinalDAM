package com.example.deezerproyecto.models

import java.io.Serializable

data class Playlist(
    var nombre: String,
    var canciones: MutableList<Track>,
    var rutaFoto: String,
    var esPrivada: Boolean
) : Serializable
