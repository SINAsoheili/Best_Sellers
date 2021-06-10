package net.sinasoheili.best_sellers.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import net.sinasoheili.best_sellers.webService.*

@Module
@InstallIn(ActivityComponent::class)
object MapperModule {

    @Provides
    fun getSellerMapper(): SellerMapper {
        return SellerMapper()
    }

    @Provides
    fun getUserMapper(): UserMapper {
        return UserMapper()
    }

    @Provides
    fun getShopMapper(): ShopMapper {
        return ShopMapper()
    }

    @Provides
    fun getCategoryMapper(): CategoryMapper {
        return CategoryMapper()
    }

    @Provides
    fun getDiscountMapper(): DiscountMapper {
        return DiscountMapper()
    }

    @Provides
    fun getStatisticMapper(): StatisticMapper {
        return StatisticMapper()
    }

    @Provides
    fun getMessageMapper(): MessageMapper {
        return MessageMapper()
    }

    @Provides
    fun getCriteriaMapper(): CriteriaMapper {
        return CriteriaMapper()
    }

    @Provides
    fun getShopDiscountMapper(): ShopDiscountMapper {
        return ShopDiscountMapper()
    }

    @Provides
    fun getQuestionMapper(): QuestionMapper {
        return QuestionMapper()
    }
}