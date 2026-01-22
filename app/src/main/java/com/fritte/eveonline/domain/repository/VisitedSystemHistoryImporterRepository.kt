package com.fritte.eveonline.domain.repository

import com.fritte.eveonline.data.repo.ImportResult

interface VisitedSystemHistoryImporterRepository {
    suspend fun import(): ImportResult
}