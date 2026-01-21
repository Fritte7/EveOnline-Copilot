package com.fritte.eveonline.ui.model

data class VisitWithSystem(
    val systemId: Long,
    val systemName: String,
    val regionId: Long,
    val constellationId: Long,
    val visitedAt: String
)