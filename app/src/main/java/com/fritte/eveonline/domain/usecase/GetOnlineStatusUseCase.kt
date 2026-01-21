package com.fritte.eveonline.domain.usecase

import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import com.fritte.eveonline.domain.repository.LocationRepository

class GetOnlineStatusUseCase(
    private val esiRepo: LocationRepository
) {
    suspend operator fun invoke(characterId: Long): CharacterLocationOnline {
        return esiRepo.fetchCharacterLocationOnline(characterId)
    }
}
