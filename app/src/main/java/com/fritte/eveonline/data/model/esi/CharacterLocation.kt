package com.fritte.eveonline.data.model.esi

data class CharacterLocation (
    val solar_system_id: Long,
    val station_id: Long? = null,
    val structure_id: Long? = null
)