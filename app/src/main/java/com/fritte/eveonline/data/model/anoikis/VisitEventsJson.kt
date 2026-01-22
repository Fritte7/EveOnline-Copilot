package com.fritte.eveonline.data.model.anoikis

data class VisitEventsPayload(
    val synthetic: Boolean = true,
    val startDate: String? = null,
    val endDate: String? = null,
    val events: List<VisitEventJson>
)

data class VisitEventJson(
    val systemName: String,
    val visitedAt: Long
)