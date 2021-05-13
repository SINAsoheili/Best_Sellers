package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Message
import net.sinasoheili.best_sellers.util.Mapper

class MessageMapper: Mapper<Message, MessageEntity> {

    override fun toBase(entity: MessageEntity): Message {
        return Message(
                entity.userId,
                entity.shopId,
                entity.text
        )
    }

    override fun toEntity(base: Message): MessageEntity {
        return MessageEntity(
                base.idShop,
                base.idUser,
                base.text
        )
    }
}