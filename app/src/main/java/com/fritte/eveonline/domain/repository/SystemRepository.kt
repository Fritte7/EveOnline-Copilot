package com.fritte.eveonline.domain.repository

interface SystemRepository {
    suspend fun isJSpace(systemId: Long): Boolean
}