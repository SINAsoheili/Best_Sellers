package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Discount
import net.sinasoheili.best_sellers.model.Shop
import net.sinasoheili.best_sellers.model.ShopDiscount
import net.sinasoheili.best_sellers.util.Mapper

class ShopDiscountMapper: Mapper<ShopDiscount , UserDiscountListEntity> {

    val shopMapper: ShopMapper = ShopMapper()
    val discountMapper: DiscountMapper = DiscountMapper()

    override fun toBase(entity: UserDiscountListEntity): ShopDiscount {
        val shop: Shop = shopMapper.toBase(entity.shop)
        val discount: Discount = discountMapper.toBase(entity.discount)
        return ShopDiscount(shop , discount)
    }

    override fun toEntity(base: ShopDiscount): UserDiscountListEntity {
        val shopEntity: ShopEntity = shopMapper.toEntity(base.shop)
        val discountEntity: DiscountEntity = discountMapper.toEntity(base.discount)
        return UserDiscountListEntity(discountEntity , shopEntity)
    }
}