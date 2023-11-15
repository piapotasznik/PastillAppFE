package edu.ort.pastillapp.Services

import edu.ort.pastillapp.Models.ApiTokenResponse
import edu.ort.pastillapp.Models.Token
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface TokenService {

    @POST("api/Token")
    fun createToken(@Body token: Token): Call<Void>

    @HTTP(method = "DELETE", path = "api/Token", hasBody = true)
    fun deleteToken(@Body tokenValue: String): Call<Void>

    @GET("api/Token")
    fun getTokens(): Call<List<Token>>

    @GET("api/Token/{email}")
    fun getUserEmail(@Path("email") email: String): Call<ApiTokenResponse>
}