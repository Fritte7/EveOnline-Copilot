package com.fritte.eveonline.data.repo

import android.content.Context
import androidx.room.withTransaction
import com.fritte.eveonline.data.model.anoikis.AnoikisRootDto
import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.data.room.entities.ConstellationEntity
import com.fritte.eveonline.data.room.entities.RegionEntity
import com.fritte.eveonline.data.room.entities.SystemEntity
import com.fritte.eveonline.domain.repository.AnoikisImporterRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnoikisImporterRepositoryImpl(
    private val context: Context,
    private val db: AppDatabase,
    private val moshi: Moshi,
) : AnoikisImporterRepository {

    override suspend fun importIfNeeded() {
        withContext(Dispatchers.IO) {
            if (db.systemDao().countSystems() > 0) return@withContext

            val json = context.assets.open("anoikis.json").bufferedReader().use { it.readText() }

            val root = moshi.adapter(AnoikisRootDto::class.java)
                .fromJson(json) ?: error("Failed to parse Anoikis JSON")

            val regionEntityEntities = ArrayList<RegionEntity>(root.regions.size)
            val constellationEntityEntities = ArrayList<ConstellationEntity>(3000)
            val systemEntityEntities = ArrayList<SystemEntity>(3000)

            for (r in root.regions) {
                val constellationIds = r.constellations.map { c -> c.id }

                regionEntityEntities += RegionEntity(
                    regionId = r.id,
                    name = r.region,
                    constellationsIds = constellationIds,
                )

                for (c in r.constellations) {
                    val systemIds = c.systems.map { s -> s.id }

                    constellationEntityEntities += ConstellationEntity(
                        constellationId = c.id,
                        name = c.constellation,
                        systemsIds = systemIds,
                        regionId = r.id
                    )

                    for (s in c.systems) {
                        systemEntityEntities += SystemEntity(
                            systemId = s.id,
                            name = s.name,
                            wormholeClass = s.wormholeClass ?: r.wormholeClass ?: "unknown",
                            effect = s.effect,
                            statics = s.statics ?: emptyList(),
                            constellationId = c.id,
                            alias = s.alias
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