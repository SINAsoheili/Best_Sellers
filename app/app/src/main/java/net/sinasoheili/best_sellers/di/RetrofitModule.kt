package net.sinasoheili.best_sellers.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import net.sinasoheili.best_sellers.webService.WebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object RetrofitModule {

//    private const val BASE_URL: String = "http://10.0.2.2:5000/"
    private const val BASE_URL: String = "http://bcfd6bcd1934.ngrok.io"

    @Provides
    fun getRetrofit() : Retrofit {
        return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Provides
    fun getWebService(retrofit: Retrofit) : WebService {
        return retrofit.create(WebService::class.java)
    }
}