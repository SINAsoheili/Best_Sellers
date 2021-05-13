package net.sinasoheili.best_sellers.util

interface Mapper<Base, Entity> {

    fun toBase(entity: Entity) : Base
    fun toEntity(base: Base) : Entity
}