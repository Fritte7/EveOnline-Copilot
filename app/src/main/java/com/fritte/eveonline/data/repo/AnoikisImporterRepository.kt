package com.fritte.eveonline.data.repo

import android.content.Context
import androidx.room.withTransaction
import com.fritte.eveonline.data.model.anoikis.AnoikisRootDto
import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.data.room.entities.ConstellationEntity
import com.fritte.eveonline.data.room.entities.RegionEntity
import com.fritte.eveonline.data.room.entities.SystemEntity
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnoikisImporterRepository(
    private val context: Context,
    private val db: AppDatabase,
    private val moshi: Moshi,
) {

    suspend fun importIfNeeded(assetFileName: String = "anoikis_regions_constellations_systems.json") {
        withContext(Dispatchers.IO) {
            if (db.systemDao().countSystems() > 0) return@withContext

            val json = context.assets.open(assetFileName).bufferedReader().use { it.readText() }

            val root = moshi.adapter(AnoikisRootDto::class.java)
                .fromJson(json) ?: error("Failed to parse Anoikis JSON")

            val regionEntityEntities = ArrayList<RegionEntity>(root.regions.size)
            val constellationEntityEntities = ArrayList<ConstellationEntity>(3000)
            val systemEntityEntities = ArrayList<SystemEntity>(3000)

            for (r in root.regions) {
                val constellationNames =
                    r.constellations.mapNotNull { c -> c.constellation.ifBlank { null } }

                regionEntityEntities += RegionEntity(
                    name = r.region,
                    constellations = constellationNames
                )

                for (c in r.constellations) {
                    val systemNames = c.systems.mapNotNull { s -> s.name.ifBlank { null } }

                    constellationEntityEntities += ConstellationEntity(
                        name = c.constellation,
                        systems = systemNames,
                        region = r.region
                    )

                    for (s in c.systems) {
                        val sysName = s.name.ifBlank { null } ?: continue

                        systemEntityEntities += SystemEntity(
                            name = sysName,
                            wormholeClass = s.wormholeClass ?: r.wormholeClass ?: "unknown",
                            effect = s.effect,
                            statics = s.statics ?: emptyList(),
                            constellation = c.constellation
                        )
                    }
                }
            }

            db.withTransaction {
                db.regionDao().insertAll(regionEntityEntities)
                db.constellationDao().insertAll(constellationEntityEntities)
                db.systemDao().insertAll(systemEntityEntities)
            }
        }
    }
}