package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.util.Mapper
import net.sinasoheili.best_sellers.model.Shop

class ShopMapper : Mapper<Shop, ShopEntity> {

    override fun toBase(entity: ShopEntity): Shop {
        return Shop(
            name = entity.name,
            address = entity.address,
            idSeller = entity.idSeller,
            idCategory = entity.idCategory,
            phone = entity.phone ?: "",
            description = entity.description ?: "",
            site = entity.site ?: "",
            latitude = entity.latitude,
            longitude = entity.longitude,
            id = entity.id
        )
    }

    override fun toEntity(base: Shop): ShopEntity {
        return ShopEntity(
            id = base.id,
            name = base.name,
            address = base.address,
            phone = base.phone,
            idSeller = base.idSeller,
            idCategory = base.idCategory,
            description = base.description,
            site = base.site,
            latitude = base.latitude,
            longitude = base.longitude
        )
    }
}