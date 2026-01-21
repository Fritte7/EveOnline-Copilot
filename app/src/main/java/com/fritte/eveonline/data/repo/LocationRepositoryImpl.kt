package com.fritte.eveonline.data.repo

import com.fritte.eveonline.data.model.esi.CharacterLocation
import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import com.fritte.eveonline.data.network.api.EVEEsiAPI
import com.fritte.eveonline.domain.repository.LocationRepository

class LocationRepositoryImpl(
    private val api: EVEEsiAPI
) : LocationRepository {

    override suspend fun fetchCharacterLocationOnline(characterID: Long): CharacterLocationOnline {
        return api.getCharacterOnline(characterID)
    }

    override suspend fun fetchCharacterLocation(characterID: Long): CharacterLocation {
        return api.getCharacterLocation(characterID)
    }
}