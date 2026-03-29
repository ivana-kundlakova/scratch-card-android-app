package sk.ikundlakova.scratchcardapp.data.api.response.mapper

import sk.ikundlakova.scratchcardapp.data.api.response.ActivationResponseSerializable
import sk.ikundlakova.scratchcardapp.domain.networking.response.ActivationResponse

fun ActivationResponseSerializable.toDomain(): ActivationResponse =
    ActivationResponse(android = android)
