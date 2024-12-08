package com.example.tracker_data.repository

import android.annotation.SuppressLint
import com.example.tracker_data.local.TrackerDao
import com.example.tracker_data.mapper.toTrackableFood
import com.example.tracker_data.mapper.toTrackedFood
import com.example.tracker_data.mapper.toTrackedFoodEntity
import com.example.tracker_data.remote.OpenFoodApi
import com.example.tracker_domain.model.TrackableFood
import com.example.tracker_domain.model.TrackedFood
import com.example.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val dao: TrackerDao,
    private val api: OpenFoodApi
): TrackerRepository {
    override suspend fun searchFod(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val searchDTO = api.searchFood(
                query = query,
                page = page,
                pageSize = pageSize
            )
            Result.success(
                searchDTO.products.mapNotNull { it.toTrackableFood() }
            )
        } catch (e: Exception){
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(trackedFoodEntity = food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(trackedFoodEntity = food.toTrackedFoodEntity())
    }

    @SuppressLint("NewApi")
    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { it.toTrackedFood() }
        }
    }
}










