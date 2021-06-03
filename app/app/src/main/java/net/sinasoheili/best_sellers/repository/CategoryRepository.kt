package net.sinasoheili.best_sellers.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.Criteria
import net.sinasoheili.best_sellers.model.ShopCategory
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.webService.*
import java.lang.Exception

class CategoryRepository constructor(
    private val context: Context,
    private val webService: WebService,
    private val categoryMapper: CategoryMapper,
    private val criteriaMapper: CriteriaMapper
){
    suspend fun getShopCategories() : Flow<DataState<List<ShopCategory>>> = flow {
        emit(DataState.Loading())
        delay(1000)

        try {
            val response : CategoryEntityResponse = webService.getCategories()
            val catEntity: Array<CategoryEntity> = response.arr

            if (catEntity.size != 0) {

                val categoryArray: MutableList<ShopCategory> = mutableListOf()

                for (item in catEntity) {
                    categoryArray.add(categoryMapper.toBase(item))
                }

                emit(DataState.Success<List<ShopCategory>>(categoryArray))

            } else {
                emit(DataState.Error(context.getString(R.string.category_not_found)))
            }

        } catch (e: Exception) {

            emit(DataState.ConnectionError(e))
        }
    }

    suspend fun getCriteria(categoryId: Int) : Flow<DataState<MutableList<Criteria>>> = flow {
        emit(DataState.Loading())
        delay(1000)
        try {
            val criteriasResponse: CategoryCriteriaResponse = webService.getCriteriaOfCategory(categoryId)
            val response: MutableList<Criteria> =  mutableListOf()
            for (i in criteriasResponse.criterias) {
                response.add(criteriaMapper.toBase(i))
            }
            emit(DataState.Success(response))
        } catch (e: Exception) {
            emit(DataState.ConnectionError(e))
        }
    }
}