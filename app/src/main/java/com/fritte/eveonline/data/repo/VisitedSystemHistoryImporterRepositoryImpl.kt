package com.fritte.eveonline.data.repo

import android.content.Context
import androidx.room.withTransaction
import com.fritte.eveonline.data.model.anoikis.VisitEventsPayload
import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity
import com.fritte.eveonline.domain.repository.VisitedSystemHistoryImporterRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VisitedSystemHistoryImporterRepositoryImpl(
    private val context: Context,
    private val db: AppDatabase,
    private val moshi: Moshi,
) : VisitedSystemHistoryImporterRepository {

    override suspend fun import(): ImportResult =
        withContext(Dispatchers.IO) {
            val batchSize = 1000
            val json = context.assets
                .open("visited_system.json")
                .bufferedReader()
                .use { it.readText() }

            val payload = moshi.adapter(VisitEventsPayload::class.java)
                .fromJson(json)
                ?: error("Failed to parse visited_system JSON")
            val systems = db.systemDao().getAll()

            val systemNameToId: Map<String, Long> = buildMap {
                for (s in systems) {
                    // normal Anoikis systems: name is Jxxxxxx
                    put(s.name, s.systemId)
                    // drifter systems: alias is Jxxxxxx
                    s.alias?.let { put(it, s.systemId) }
                }
            }

            val missing = LinkedHashSet<String>()
            var inserted = 0

            db.withTransaction {
                db.visitedSystemDao().clearAll()

                val buffer = ArrayList<VisitedSystemEntity>(batchSize)

                for (ev in payload.events) {
                    val id = systemNameToId[ev.systemName]
                    if (id == null) {
                        missing += ev.systemName
                        continue
                    }

                    buffer += VisitedSystemEntity(
                        systemId = id,
                        visitedAt = ev.visitedAt
                    )

                    if (buffer.size >= batchSize) {
                        db.visitedSystemDao().insertVisits(buffer)
                        inserted += buffer.size
                        buffer.clear()
                    }
                }

                if (buffer.isNotEmpty()) {
                    db.visitedSystemDao().insertVisits(buffer)
                    inserted += buffer.size
                    buffer.clear()
                }

                if (missing.isNotEmpty()) {
                    error(
                        "Missing systems in DB (name -> systemId not found): ${missing.take(30)}" +
                                (if (missing.size > 30) " ... +${missing.size - 30} more" else "")
                    )
                }
            }

            // This is the value returned by withContext, and thus by import()
            ImportResult(
                insertedEvents = inserted,
                missingSystems = missing.toList()
            )
        }
}

data class ImportResult(
    val insertedEvents: Int,
    val missingSystems: List<String>
)
