package com.fritte.eveonline.data.repo

import android.content.Context
import androidx.room.withTransaction
import com.fritte.eveonline.data.model.anoikis.VisitEventJson
import com.fritte.eveonline.data.model.anoikis.VisitEventsPayload
import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.data.room.entities.VisitedSystemEntity
import com.fritte.eveonline.domain.repository.VisitedSystemHistoryImportExportRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.sink
import java.io.File

class VisitedSystemHistoryImportExportRepositoryImpl(
    private val context: Context,
    private val db: AppDatabase,
    private val moshi: Moshi,
) : VisitedSystemHistoryImportExportRepository {

    val jsonName = "visited_system.json"

    override suspend fun import(): ImportExportResult =
        withContext(Dispatchers.IO) {
            val batchSize = 1000
            val json = context.assets
                .open(jsonName)
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
                //Clear the database before inserting
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

            ImportExportResult(
                done = true
            )
        }

    override suspend fun export(): ImportExportResult =
        withContext(Dispatchers.IO) {
            val rows = db.visitedSystemDao().getVisitsForExport()

            val payload = VisitEventsPayload(
                events = rows.map { row ->
                    VisitEventJson(
                        systemName = row.systemName,
                        visitedAt = row.visitedAt
                    )
                }
            )

            val adapter = moshi.adapter(VisitEventsPayload::class.java).indent("  ")
            val json = adapter.toJson(payload)

            val outDir = File(context.getExternalFilesDir(null), "exports").apply { mkdirs() }
            val outFile = File(outDir, jsonName)

            outFile.sink().buffer().use { it.writeUtf8(json) }

            ImportExportResult(
                done = true
            )
        }
}

data class ImportExportResult(
    val done: Boolean
)
