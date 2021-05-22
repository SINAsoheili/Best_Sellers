package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Statistic
import net.sinasoheili.best_sellers.util.Mapper

class StatisticMapper : Mapper<Statistic , StatisticEntity> {
    override fun toBase(entity: StatisticEntity): Statistic {
        return Statistic(
                title = entity.name,
                amount = entity.amount
        )
    }

    override fun toEntity(base: Statistic): StatisticEntity {
        return StatisticEntity(
                name = base.title,
                amount = base.amount
        )
    }
}