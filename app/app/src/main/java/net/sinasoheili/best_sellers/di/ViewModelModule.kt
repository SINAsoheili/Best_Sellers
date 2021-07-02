package net.sinasoheili.best_sellers.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import net.sinasoheili.best_sellers.repository.*
import net.sinasoheili.best_sellers.viewModel.*

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
    fun provideRegisterShopViewModel (@ApplicationContext context: Context,
                                      shopRepository: ShopRepository,
                                      categoryRepository: CategoryRepository) : RegisterShopViewModel {
        return RegisterShopViewModel(context , shopRepository , categoryRepository)
    }

    @Provides
    fun provideSellerStoreFragmentViewModel (shopRepository: ShopRepository ,
                                             sellerRepository: SellerRepository) : SellerStoreFragmentViewModel {
        return SellerStoreFragmentViewModel(shopRepository, sellerRepository)
    }

    @Provides
    fun provideSellerDashboardFragmentViewModel (discountRepository: DiscountRepository) : SellerDashboardFragmentViewModel {
        return SellerDashboardFragmentViewModel(discountRepository)
    }

    @Provides
    fun provideSellerStatisticViewModel (statisticRepository: StatisticRepository ,
                                         messageRepository: MessageRepository) : SellerStatisticViewModel {
        return SellerStatisticViewModel(statisticRepository , messageRepository)
    }

    @Provides
    fun provideUserMainPageViewModel (userRepository: UserRepository , shopRepository: ShopRepository) : UserMainPageViewModel {
        return UserMainPageViewModel(userRepository , shopRepository)
    }

    @Provides
    fun provideShopSearchViewModel (categoryRepository: CategoryRepository , shopRepository: ShopRepository) : ShopSearchViewModel {
        return ShopSearchViewModel(categoryRepository , shopRepository)
    }

    @Provides
    fun provideUserDiscountViewModel (discountRepository: DiscountRepository) : UserDiscountViewModel {
        return UserDiscountViewModel(discountRepository)
    }

    @Provides
    fun provideSurveyViewModel (questionRepository: QuestionRepository ,
                                messageRepository: MessageRepository , discountRepository: DiscountRepository
    ) : SurveyViewModel {
        return SurveyViewModel(messageRepository, questionRepository, discountRepository)
    }

    @Provides
    fun provideShowQRcodeViewModel (@ActivityContext context: Context): ShowQRcodeViewModel {
        return ShowQRcodeViewModel(context)
    }
}