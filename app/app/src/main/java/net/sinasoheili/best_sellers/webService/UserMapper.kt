package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.User
import net.sinasoheili.best_sellers.util.Mapper

class UserMapper: Mapper<User, UserEntity> {

    override fun toBase(entity: UserEntity): User {
        return User(
                entity.id,
                entity.name,
                entity.lastName,
                entity.phone
        )
    }

    override fun toEntity(base: User): UserEntity {
        return UserEntity(
                base.id,
                base.name,
                base.lastName,
                base.phone
        )
    }
}