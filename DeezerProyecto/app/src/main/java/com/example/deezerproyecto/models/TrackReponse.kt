package com.example.deezerproyecto.models

data class TrackResponse(
    val data: List<Track>,
    val total: Int
)

data class Track(
    val id: Long,
    val title: String,
    val artist: Artist,
    val album: Album,
    val duration: Int,
    val preview: String  // 🔄 Añadido para el audio de Deezer
)

data class Artist(
    val id: Long,
    val name: String,
    val picture: String
)

data class Album(
    val id: Long,
    val title: String,
    val cover: String
)
