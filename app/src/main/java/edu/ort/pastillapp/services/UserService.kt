      package edu.ort.pastillapp.services
      import edu.ort.pastillapp.models.ApiUserResponse
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
            // llamar al metodo para guardar el token COMPLETAR
            // @POST("api/sendFCMToken")
            // fun sendFCMToken(@Query("email") email: String, @Query("token") token: String): Call<ApiUserResponse>
      }
