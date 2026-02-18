package com.fritte.eveonline.domain.repository

import com.fritte.eveonline.data.repo.ImportExportResult

interface VisitedSystemHistoryImportExportRepository {
    suspend fun import(): ImportExportResult
    suspend fun export(): ImportExportResult
}