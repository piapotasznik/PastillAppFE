package edu.ort.pastillapp.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object ActivityServiceApiBuilder {

    // CAMBIAR POR DIRECCION IP LOCAL PROPIA!!!!!!!!!!!!! HABILITAR FIREWALL DE WINDOWS
     private val BASE_URL = "https://cdcf-2800-21c1-c400-77b-d08a-bdd9-83bb-76a0.ngrok-free.app"
    //VIEJO "https://77f5-2800-21c1-c400-77b-44c3-2117-2cf5-63a7.ngrok-free.app"
    // NUEVO "https://3fe3-2800-21c1-c400-77b-49d4-7abe-8867-ed8c.ngrok-free.app"
    // 23/10 https://cdcf-2800-21c1-c400-77b-d08a-bdd9-83bb-76a0.ngrok-free.app
    // "https://192.168.0.96:7067"
    //https://3fe3-2800-21c1-c400-77b-49d4-7abe-8867-ed8c.ngrok-free.app/swagger/index.html

    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    var client : OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(interceptor).addInterceptor(interceptor)
    }.build()

     fun create(): UserService {
         val retrofit = Retrofit.Builder()
             .baseUrl(BASE_URL)
             .addConverterFactory(GsonConverterFactory.create())
             .client(getUnsafeOkHttpClient())
             //PARA ACTIVAR SEGURIDAD DE NUEVO: COMENTAR LINEA 41
             // Y REEMPLAZAR POR "CLIENT" PARA VALIDAR CERTIFICADOS
             .build()

         return retrofit.create(UserService::class.java)

     }

    fun createReminder(): ReminderService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            //PARA ACTIVAR SEGURIDAD DE NUEVO: COMENTAR LINEA 41
            // Y REEMPLAZAR POR "CLIENT" PARA VALIDAR CERTIFICADOS
            .build()
        return retrofit.create(ReminderService::class.java)
    }

    fun createToken(): TokenService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient())
            //PARA ACTIVAR SEGURIDAD DE NUEVO: COMENTAR LINEA 41
            // Y REEMPLAZAR POR "CLIENT" PARA VALIDAR CERTIFICADOS
            .build()
        return retrofit.create(TokenService::class.java)
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient? {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()
                .apply {
                    addInterceptor(interceptor)
                }

            builder.sslSocketFactory(
                sslSocketFactory,
                trustAllCerts[0] as X509TrustManager
            )
            builder.hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
            builder.build()
        } catch (e: KeyManagementException) {
            throw RuntimeException(e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}
