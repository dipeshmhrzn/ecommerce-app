package com.example.ecommerce.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Named

class StripeService @Inject constructor(
    @Named("Stripe") private val client: HttpClient,
    private val secretKey: String
) {
    suspend fun createPaymentIntent(amount: Int): Result<String> {
        return try {
            val response: PaymentIntentResponse = client.post("payment_intents") {
                header(HttpHeaders.Authorization, "Bearer $secretKey")
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    FormDataContent(
                        Parameters.build {
                            append("amount", amount.toString())
                            append("currency", "usd")
                            append("payment_method_types[]", "card")
                        }
                    )
                )
            }.body()

            Result.success(response.client_secret)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Serializable
    data class PaymentIntentResponse(
        val client_secret: String
    )
}

