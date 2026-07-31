package ec.edu.uisek.githubclient.services

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.github.com/"

    private lateinit var authService: AuthService

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun init(context: Context) {
        authService = AuthService(context)
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->

            val requestBuilder = chain.request()
                .newBuilder()
                .header("Cache-Control", "no-cache")
                .header("Pragma", "no-cache")
                .header("Expires", "0")

            if (::authService.isInitialized) {

                val token = authService.getToken()

                if (!token.isNullOrEmpty()) {
                    requestBuilder.header(
                        "Authorization",
                        "Bearer $token"
                    )
                }
            }

            chain.proceed(requestBuilder.build())
        }
        .build()


    val apiService: ApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(ApiService::class.java)
    }
}