package com.fritte.eveonline.data

import android.content.Context
import com.fritte.eveonline.data.model.anoikis.AnoikisRootDto
import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.data.room.entities.Constellation
import com.fritte.eveonline.data.room.entities.Region
import com.fritte.eveonline.data.room.entities.System
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.room.withTransaction

class AnoikisImporter(
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

            val regionEntities = ArrayList<Region>(root.regions.size)
            val constellationEntities = ArrayList<Constellation>(3000)
            val systemEntities = ArrayList<System>(3000)

            for (r in root.regions) {
                val constellationNames = r.constellations.mapNotNull { c -> c.constellation.ifBlank { null } }

                regionEntities += Region(
                    name = r.region,
                    constellations = constellationNames
                )

                for (c in r.constellations) {
                    val systemNames = c.systems.mapNotNull { s -> s.name.ifBlank { null } }

                    constellationEntities += Constellation(
                        name = c.constellation,
                        systems = systemNames,
                        region = r.region
                    )

                    for (s in c.systems) {
                        val sysName = s.name.ifBlank { null } ?: continue

                        systemEntities += System(
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
                db.regionDao().insertAll(regionEntities)
                db.constellationDao().insertAll(constellationEntities)
                db.systemDao().insertAll(systemEntities)
            }
        }
    }
}
