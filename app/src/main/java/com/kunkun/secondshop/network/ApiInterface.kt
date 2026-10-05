package com.kunkun.secondshop.network

import com.kunkun.secondshop.data.model.Category
import com.kunkun.secondshop.data.model.Product
import com.kunkun.secondshop.util.KunkunConstants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient

// Interface endpoint Retrofit
interface ApiInterface {

    @GET("data/categories.json")
    suspend fun getCategories(): List<Category>

    @GET("data/products.json")
    suspend fun getProducts(): List<Product>
}

// Singleton Retrofit instance
object ApiClient {

    val instance: ApiInterface by lazy {

        val okHttpClient = OkHttpClient.Builder().apply {
            if (KunkunConstants.USE_MOCK) {
                addInterceptor(MockInterceptor())
            }
        }.build()

        Retrofit.Builder()
            .baseUrl(KunkunConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}
