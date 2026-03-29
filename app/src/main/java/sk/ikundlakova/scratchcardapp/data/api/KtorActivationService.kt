package sk.ikundlakova.scratchcardapp.data.api

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import sk.ikundlakova.scratchcardapp.data.api.response.ActivationResponseSerializable
import sk.ikundlakova.scratchcardapp.data.api.response.mapper.toDomain
import sk.ikundlakova.scratchcardapp.domain.networking.ActivationService
import sk.ikundlakova.scratchcardapp.domain.networking.response.ActivationResponse
import sk.ikundlakova.scratchcardapp.domain.networking.util.DataError
import sk.ikundlakova.scratchcardapp.domain.networking.util.Result

class KtorActivationService(
    private val client: HttpClient = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                prettyPrint = true
            })
        }
    }
) : ActivationService {

    private val baseUrl = "https://api.o2.sk"

    companion object {
        private const val API_ROUTE_ACTIVATE = "/version"
    }

    override suspend fun activateCard(code: String): Result<ActivationResponse, DataError.Remote> {
        val response: ActivationResponseSerializable = try {
             client.get("$baseUrl$API_ROUTE_ACTIVATE") {
                parameter("code", code)
            }.body()
        } catch (e: Exception) {
            Log.e("KtorActivationService", "${e.message}")
            return Result.Failure(DataError.Remote.UNKNOWN)
        }
        return Result.Success(response.toDomain())
    }
}
