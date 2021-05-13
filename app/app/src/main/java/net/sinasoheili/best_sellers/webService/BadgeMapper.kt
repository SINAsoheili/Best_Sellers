package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Badge
import net.sinasoheili.best_sellers.util.Mapper

class BadgeMapper: Mapper<Badge, BadgeEntity> {

    override fun toBase(entity: BadgeEntity): Badge {
        return Badge(
                entity.id,
                entity.name,
                entity.minScore,
                entity.category
        )
    }

    override fun toEntity(base: Badge): BadgeEntity {
        return BadgeEntity(
                base.id,
                base.name,
                base.category,
                base.minScore
        )
    }
}