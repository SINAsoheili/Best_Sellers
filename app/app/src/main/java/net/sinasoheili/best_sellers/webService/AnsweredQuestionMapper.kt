package net.sinasoheili.best_sellers.webService

import net.sinasoheili.best_sellers.model.AnsweredQuestion
import net.sinasoheili.best_sellers.util.Mapper

class AnsweredQuestionMapper : Mapper<AnsweredQuestion , AnsweredQuestionEntity> {
    override fun toBase(entity: AnsweredQuestionEntity): AnsweredQuestion {
        return AnsweredQuestion(
                content =  entity.content,
                questionId = entity.questionId,
                score = entity.score
        )
    }

    override fun toEntity(base: AnsweredQuestion): AnsweredQuestionEntity {
        return AnsweredQuestionEntity(
                content =  base.content,
                questionId = base.questionId,
                score = base.score
        )
    }
}