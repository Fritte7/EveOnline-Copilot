package com.fritte.eveonline.data.network.api

import com.fritte.eveonline.data.model.esi.CharacterLocation
import com.fritte.eveonline.data.model.esi.CharacterLocationOnline
import retrofit2.http.GET
import retrofit2.http.Path

interface EVEEsiAPI {

    @GET("/characters/{character_id}/online")
    suspend fun getCharacterOnline(
        @Path("character_id") characterId: Long
    ): CharacterLocationOnline

    @GET("/characters/{character_id}/location/")
    suspend fun getCharacterLocation(
        @Path("character_id") characterId: Long
    ): CharacterLocation
}