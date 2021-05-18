package net.sinasoheili.best_sellers.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.model.ShopCategory
import net.sinasoheili.best_sellers.util.DataState
import net.sinasoheili.best_sellers.webService.CategoryEntity
import net.sinasoheili.best_sellers.webService.CategoryEntityResponse
import net.sinasoheili.best_sellers.webService.CategoryMapper
import net.sinasoheili.best_sellers.webService.WebService
import java.lang.Exception

class CategoryRepository constructor(
    private val context: Context,
    private val webService: WebService,
    private val categoryMapper: CategoryMapper
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
}