package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.ShopCategory
import net.sinasoheili.best_sellers.util.Mapper

class CategoryMapper: Mapper<ShopCategory, CategoryEntity> {

    override fun toBase(entity: CategoryEntity): ShopCategory {
        return ShopCategory(
            id = entity.id,
            name = entity.name
        )
    }

    override fun toEntity(base: ShopCategory): CategoryEntity {
        return CategoryEntity(
            id = base.id,
            name = base.name
        )
    }
}