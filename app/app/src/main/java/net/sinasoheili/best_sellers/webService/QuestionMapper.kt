package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.Question
import net.sinasoheili.best_sellers.util.Mapper

class QuestionMapper: Mapper<Question , QuestionEntity> {

    override fun toBase(entity: QuestionEntity): Question {
        return Question(
                entity.questionId,
                entity.text
        )
    }

    override fun toEntity(base: Question): QuestionEntity {
        return QuestionEntity(
                base.text,
                base.id
        )
    }
}