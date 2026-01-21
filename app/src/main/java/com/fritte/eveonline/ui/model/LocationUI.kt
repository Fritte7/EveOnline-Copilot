package com.fritte.eveonline.ui.model

data class LocationUI(
    val title: String,
    val subtitle: String? = null,
    val systemName: String? = null,
    val systemClass: String? = null,
    val systemEffect: String? = null,
    val isStale: Boolean = false,
)
