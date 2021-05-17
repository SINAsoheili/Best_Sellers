package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Seller
import net.sinasoheili.best_sellers.util.Mapper

class SellerMapper: Mapper<Seller, SellerEntity> {

    override fun toBase(entity: SellerEntity): Seller {
        return Seller(
                entity.name,
                entity.lastName,
                entity.phone,
                entity.id
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