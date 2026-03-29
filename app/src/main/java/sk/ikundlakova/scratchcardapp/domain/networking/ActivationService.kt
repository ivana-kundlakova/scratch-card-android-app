package sk.ikundlakova.scratchcardapp.domain.networking

import sk.ikundlakova.scratchcardapp.domain.networking.response.ActivationResponse
import sk.ikundlakova.scratchcardapp.domain.networking.util.DataError
import sk.ikundlakova.scratchcardapp.domain.networking.util.Result

interface ActivationService {
    suspend fun activateCard(code: String): Result<ActivationResponse, DataError.Remote>
}