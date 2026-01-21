package com.fritte.eveonline.domain.repository

import com.fritte.eveonline.data.model.esi.CharacterLocation
import com.fritte.eveonline.data.model.esi.CharacterLocationOnline

interface LocationRepository {
    suspend fun fetchCharacterLocationOnline(characterID: Long): CharacterLocationOnline
    suspend fun fetchCharacterLocation(characterID: Long): CharacterLocation
}