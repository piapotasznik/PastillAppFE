      package edu.ort.pastillapp.services
      import edu.ort.pastillapp.models.ApiUserResponse
      import edu.ort.pastillapp.models.ApiContactEmergencyRequest
      import edu.ort.pastillapp.models.ApiEmergencyContactResponseDTO
      import edu.ort.pastillapp.models.ApiContactEmergencyList
      import edu.ort.pastillapp.models.ApiContactEmergencyServerResponse
      import edu.ort.pastillapp.models.Token
      import edu.ort.pastillapp.models.User
      import retrofit2.Call
      import retrofit2.http.Body
      import retrofit2.http.DELETE
      import retrofit2.http.GET
      import retrofit2.http.POST
      import retrofit2.http.PUT
      import retrofit2.http.Path
      import retrofit2.http.Query

      interface UserService {
            @GET("api/User/{userId}")
            fun getUserId(@Path("userId") userId: Int): Call<ApiUserResponse>
            @GET("api/User/{email}/user")
            fun getUserEmail(@Path("email") email: String): Call<ApiUserResponse>
            @GET("api/User")
            fun getUsers(): Call<List<ApiUserResponse>>
            @DELETE("api/User/{userId}")
            fun deleteUserId(@Path("userId") userId: Int): Call<Void>
            @POST("api/User")
            fun createUser(@Body user: User): Call<Void>
            @PUT("api/User/{userId}")
            fun updateUser(@Path("userId") userId: Int, @Body user: User): Call<Void>
            @POST("api/Token")
            fun sendTokenToServer(@Body token: Token): Call<Void>
            @POST("api/User/contact-emergency")
            fun contactEmergency(@Body contactEmergencyRequest: ApiContactEmergencyRequest): Call<Void>
            @GET("api/User/contact-emergency/request")
            fun getEmergencyRequests(@Query("userMail") userMail: String): Call<ApiContactEmergencyList>
            @POST("api/User/contact-emergency/request")
            fun sendEmergencyRequestResponse(@Body request: ApiEmergencyContactResponseDTO): Call<ApiContactEmergencyServerResponse>

      }
