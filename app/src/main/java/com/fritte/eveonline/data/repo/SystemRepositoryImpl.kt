package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.room.AppDatabase
import com.fritte.eveonline.domain.repository.SystemRepository

class SystemRepositoryImpl(
    private val db: AppDatabase,
) : SystemRepository {

    override suspend fun isJSpace(systemId: Long): Boolean {
        return db.systemDao().getSystemById(systemId) != null
    }
}