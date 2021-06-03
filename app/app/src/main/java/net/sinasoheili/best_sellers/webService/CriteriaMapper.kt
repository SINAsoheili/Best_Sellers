package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Criteria
import net.sinasoheili.best_sellers.util.Mapper

class CriteriaMapper : Mapper<Criteria, CriteriaEntity> {

    override fun toBase(entity: CriteriaEntity): Criteria {
        return Criteria(
                id = entity.id,
                name = entity.name
        )
    }

    override fun toEntity(base: Criteria): CriteriaEntity {
        return CriteriaEntity(
                id = base.id,
                name = base.name
        )
    }
}