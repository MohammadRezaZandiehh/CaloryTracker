package com.example.tracker_presentation.repository

import androidx.compose.runtime.MutableState
import com.example.tracker_domain.model.TrackableFood
import com.example.tracker_domain.model.TrackedFood
import com.example.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDate

class TrackerRepositoryFake: TrackerRepository {

    var shouldReturnError = false
    private val trackedFood = mutableListOf<TrackedFood>()
    var searchResult = listOf<TrackableFood>()
    private val getFoodsForDateFlow =
        MutableSharedFlow<List<TrackedFood>>(replay = 1)

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return if (shouldReturnError)
            Result.failure(Throwable())
        else
            Result.success(searchResult)
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        trackedFood.add(food)
        getFoodsForDateFlow.emit(trackedFood)
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        trackedFood.remove(food)
        getFoodsForDateFlow.emit(trackedFood)
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return getFoodsForDateFlow
    }
}







