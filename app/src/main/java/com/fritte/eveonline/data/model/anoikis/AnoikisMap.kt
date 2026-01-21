package com.fritte.eveonline.data.model.anoikis

data class AnoikisRootDto(
    val regions: List<AnoikisRegionDto>
)

data class AnoikisRegionDto(
    val id: Long,
    val region: String,
    val wormholeClass: String? = null,
    val constellations: List<AnoikisConstellationDto>
)

data class AnoikisConstellationDto(
    val id: Long,
    val constellation: String,
    val systems: List<AnoikisSystemDto>
)

data class AnoikisSystemDto(
    val id: Long,
    val name: String,
    val wormholeClass: String? = null,
    val statics: List<String>? = null,
    val effect: String? = null,
    val alias: String? = null,
)
