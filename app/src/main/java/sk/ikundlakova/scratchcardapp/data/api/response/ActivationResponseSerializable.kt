package sk.ikundlakova.scratchcardapp.data.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivationResponseSerializable(
    @SerialName("android") val android: String? = null
)