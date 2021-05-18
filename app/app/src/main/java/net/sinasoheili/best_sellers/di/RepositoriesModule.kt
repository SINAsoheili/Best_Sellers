package net.sinasoheili.best_sellers.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import net.sinasoheili.best_sellers.repository.CategoryRepository
import net.sinasoheili.best_sellers.repository.SellerRepository
import net.sinasoheili.best_sellers.repository.ShopRepository
import net.sinasoheili.best_sellers.repository.UserRepository
import net.sinasoheili.best_sellers.webService.*

@Module
@InstallIn(ActivityComponent::class)
object RepositoriesModule {

    @Provides
    fun getRegisterSellerRepository(@ActivityContext context: Context,
                                    webService: WebService,
                                    sellerMapper: SellerMapper) : SellerRepository {
        return SellerRepository(context, webService, sellerMapper)
    }

    @Provides
    fun getUserRepository(@ActivityContext context: Context,
                          webService: WebService,
                          userMapper: UserMapper): UserRepository {
        return UserRepository(context, webService, userMapper)
    }

    @Provides
    fun  getShopRepository(@ActivityContext constext: Context,
                           webService: WebService,
                           mapper: ShopMapper) : ShopRepository {
        return ShopRepository(constext, webService, mapper)
    }

    @Provides
    fun getCategoryRepository(@ActivityContext constext: Context,
                              webService: WebService,
                              mapper: CategoryMapper) : CategoryRepository {
        return CategoryRepository(constext, webService, mapper)
    }
}