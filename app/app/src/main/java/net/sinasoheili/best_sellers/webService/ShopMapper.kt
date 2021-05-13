package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.util.Mapper
import net.sinasoheili.best_sellers.model.Shop

class ShopMapper : Mapper<Shop, ShopEntity> {

    override fun toBase(entity: ShopEntity): Shop {
        return Shop(
                entity.id,
                entity.name,
                entity.address,
                entity.phone,
                entity.idSeller,
                entity.idCategory
        )
    }

    override fun toEntity(base: Shop): ShopEntity {
        return ShopEntity(
                base.id,
                base.name,
                base.address,
                base.phone,
                base.idSeller,
                base.idCategory,
                base.description,
                base.site,
                base.latitude,
                base.longitude
        )
    }
}