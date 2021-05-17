package net.sinasoheili.best_sellers.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import net.sinasoheili.best_sellers.repository.SellerRepository
import net.sinasoheili.best_sellers.repository.UserRepository
import net.sinasoheili.best_sellers.viewModel.SetRoleViewModel
import net.sinasoheili.best_sellers.webService.SellerMapper
import net.sinasoheili.best_sellers.webService.UserMapper
import net.sinasoheili.best_sellers.webService.WebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ActivityComponent::class)
object RetrofitModule {

    private const val BASE_URL: String = "http://10.0.2.2:5000/"

    //Retrofits
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

    //Mapper
    @Provides
    fun getSellerMapper(): SellerMapper {
        return SellerMapper()
    }

    @Provides
    fun getUserMapper(): UserMapper {
        return UserMapper()
    }

    //Repositories
    @Provides
    fun getRegisterSellerRepository(@ActivityContext context: Context , webService:WebService , sellerMapper: SellerMapper) : SellerRepository {
        return SellerRepository(context, webService, sellerMapper)
    }

    @Provides
    fun getUserRepository(@ActivityContext context: Context, webService: WebService, userMapper: UserMapper): UserRepository {
        return UserRepository(context, webService, userMapper)
    }

    //ViewModels
    @Provides
    fun provideSetRoleViewModel(sellerRepository: SellerRepository , userRepository: UserRepository) : SetRoleViewModel{
        return SetRoleViewModel(sellerRepository, userRepository)
    }
}