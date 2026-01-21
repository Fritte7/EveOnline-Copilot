package com.fritte.eveonline.domain.repository

interface AnoikisImporterRepository {
    suspend fun importIfNeeded()
}