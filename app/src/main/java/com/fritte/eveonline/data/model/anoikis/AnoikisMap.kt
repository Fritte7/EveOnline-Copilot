package com.fritte.eveonline.data.model.anoikis

data class AnoikisRootDto(
    val regions: List<AnoikisRegionDto>
)

data class AnoikisRegionDto(
    val region: String,
    val wormholeClass: String? = null,
    val constellations: List<AnoikisConstellationDto>
)

data class AnoikisConstellationDto(
    val constellation: String,
    val systems: List<AnoikisSystemDto>
)

data class AnoikisSystemDto(
    val name: String,
    val wormholeClass: String? = null,
    val statics: List<String>? = null,
    val effect: String? = null
)
