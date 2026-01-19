package com.fritte.eveonline.data.model.esi

data class CharacterLocationOnline(
    val last_login :String,
    val last_logout :String,
    val logins :Int,
    val online : Boolean
)
