package com.fritte.eveonline.ui.model

interface AnoikisIndex {
    fun findBySolarSystemId(id: Long): WormholeInfo?
}

data class WormholeInfo(
    val systemName: String,
    val wormholeClass: String,
    val statics: List<String>,
    val effect: String?
)
