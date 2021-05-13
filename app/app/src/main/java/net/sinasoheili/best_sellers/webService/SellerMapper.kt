package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.util.Mapper

class SellerMapper: Mapper<Seller, SellerEntity> {

    override fun toBase(entity: SellerEntity): Seller {
        return Seller(
                entity.id,
                entity.name,
                entity.lastName,
                entity.phone
        )
    }

    override fun toEntity(base: Seller): SellerEntity {
        return SellerEntity(
                base.id,
                base.name,
                base.lastName,
                base.phone
        )
    }
}