package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.room.dao.SystemDao
import com.fritte.eveonline.domain.repository.SystemRepository

class SystemRepositoryImpl(
    private val dao: SystemDao,
) : SystemRepository {

    override suspend fun isJSpace(systemId: Long): Boolean {
        return dao.getSystemById(systemId) != null
    }
}