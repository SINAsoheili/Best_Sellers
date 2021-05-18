package net.sinasoheili.best_sellers.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import net.sinasoheili.best_sellers.repository.CategoryRepository
import net.sinasoheili.best_sellers.repository.SellerRepository
import net.sinasoheili.best_sellers.repository.ShopRepository
import net.sinasoheili.best_sellers.repository.UserRepository
import net.sinasoheili.best_sellers.viewModel.RegisterShopViewModel
import net.sinasoheili.best_sellers.viewModel.SetRoleViewModel

@Module
@InstallIn(ActivityComponent::class)
object ViewModelModule {

    @Provides
    fun provideSetRoleViewModel (@ApplicationContext context: Context ,
                                 sellerRepository: SellerRepository,
                                 userRepository: UserRepository ,
                                 shopRepository: ShopRepository) : SetRoleViewModel {
        return SetRoleViewModel(context , sellerRepository, userRepository , shopRepository)
    }

    @Provides
    fun provideRegisterShopViewModel (@ApplicationContext context: Context , shopRepository: ShopRepository , categoryRepository: CategoryRepository) : RegisterShopViewModel {
        return RegisterShopViewModel(context , shopRepository , categoryRepository)
    }
}