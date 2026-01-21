package com.fritte.eveonline.data.model.ui

data class VisitWithSystem(
    val systemId: Long,
    val systemName: String,
    val regionId: Long,
    val constellationId: Long,
    val visitedAt: String
)