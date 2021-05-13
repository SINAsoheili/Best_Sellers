package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Discount
import net.sinasoheili.best_sellers.util.Mapper

class DiscountMapper: Mapper<Discount, DiscountEntity> {

    override fun toBase(entity: DiscountEntity): Discount {
        return Discount(
                entity.shopId,
                entity.name,
                entity.amount
        )
    }

    override fun toEntity(base: Discount): DiscountEntity {
        return DiscountEntity(
                base.title,
                base.amount,
                base.idShop
        )
    }
}