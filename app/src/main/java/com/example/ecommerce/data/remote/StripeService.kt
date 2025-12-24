package com.example.ecommerce.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StripeService(private val secretKey: String) {
    private val client = okhttp3.OkHttpClient()
    private val gson = com.google.gson.Gson()
    private val baseUrl = "https://api.stripe.com/v1/"

    suspend fun createPaymentIntent(amount: Int): Result<String> {

        val formBody = okhttp3.FormBody.Builder()
            .add("amount", amount.toString())
            .add("currency", "usd")
            .add("payment_method_types[]", "card")
            .build()

        val request = okhttp3.Request.Builder()
            .url("${baseUrl}payment_intents")
            .post(formBody)
            .addHeader("Authorization", "Bearer $secretKey")
            .build()

        return withContext(Dispatchers.IO) {
            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null){
                    val paymentIntent = gson.fromJson(responseBody, PaymentIntentResponse::class.java)
                    Result.success(paymentIntent.client_secret)
                }else{
                    Result.failure(Exception("Payment intent creation failed : $responseBody"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    data class PaymentIntentResponse(
//        val id : String,
        val client_secret : String,
//        val amount : Int,
//        val currency : String,
//        val status : String
    )


}