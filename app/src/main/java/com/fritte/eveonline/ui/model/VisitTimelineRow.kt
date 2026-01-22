package com.fritte.eveonline.ui.model

data class VisitTimelineRow(
    val visitedAt: Long,
    val systemId: Long,
    val systemName: String,
    val effect: String?
)