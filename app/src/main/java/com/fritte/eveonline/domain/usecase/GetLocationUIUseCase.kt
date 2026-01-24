package com.fritte.eveonline.domain.usecase

import com.fritte.eveonline.data.model.esi.CharacterLocation
import com.fritte.eveonline.ui.model.LocationUI
import com.fritte.eveonline.data.room.dao.SystemDao
import com.fritte.eveonline.domain.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetLocationUIUseCase(
    private val esiRepo: LocationRepository,
    private val systemDao: SystemDao,
    private val recordSystemVisitUseCase: RecordSystemVisitUseCase
) {

    suspend operator fun invoke(
        characterId: Long,
        isOnline: Boolean
    ): LocationUI {
        val loc = esiRepo.fetchCharacterLocation(characterId)
        return mapLocationToUi(loc, isOnline)
    }

    suspend fun fromLocation(
        loc: CharacterLocation,
        isOnline: Boolean
    ): LocationUI {
        return mapLocationToUi(loc, isOnline)
    }

    /**
     * Fetches ESI location, enriches with local Room data (Anoikis systems),
     * and returns a UI-ready model.
     *
     * If the system is not found in Room => treated as k-space (for now).
     * If station_id/structure_id is present => Docked.
     */
    private suspend fun mapLocationToUi(
        loc: CharacterLocation,
        isOnline: Boolean
    ): LocationUI = withContext(Dispatchers.IO) {
        val stale = !isOnline
        val docked = loc.station_id != null || loc.structure_id != null
        if (docked) {
            return@withContext LocationUI(
                title = "Docked",
                subtitle = if (stale) "Last known location" else null,
                isStale = stale,
            )
        }

        // If isOnline and Room contains the system => it's a J-space we record the visit
        val sys = systemDao.getSystemById(loc.solar_system_id)
        val isSystemRecorded = if (isOnline && sys != null) { recordSystemVisitUseCase(sys.systemId) } else false

        return@withContext if (sys != null) {
            val name = sys.name
            val whClass = sys.wormholeClass
            val effect = sys.effect
            val statics = sys.statics

            LocationUI(
                title = "J-space",
                subtitle = buildString {
                    append(name)
                    append(" • $whClass")
                    if (statics.isNotEmpty()) append(" • Statics: ${statics.joinToString()}")
                    effect?.takeIf { it.isNotBlank() }?.let { append(" • Effect: $it") }
                    if (stale) append(" • Last known")
                },
                systemName = name,
                systemClass = whClass,
                systemEffect = effect,
                isNewRecorded = isSystemRecorded,
                isStale = stale,
            )
        } else {
            LocationUI(
                title = "K-space",
                subtitle = if (stale) "Last known location" else null,
                isStale = stale,
            )
        }
    }
}