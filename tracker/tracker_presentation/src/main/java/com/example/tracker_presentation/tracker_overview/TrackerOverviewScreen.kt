package com.example.tracker_presentation.tracker_overview

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.example.core.util.UiEvent
import com.example.core_ui.LocalSpacing
import com.example.core.R
import com.example.tracker_presentation.tracker_overview.component.AddButton
import com.example.tracker_presentation.tracker_overview.component.DaySelector
import com.example.tracker_presentation.tracker_overview.component.ExpandableMeal
import com.example.tracker_presentation.tracker_overview.component.NutrientsHeader
import com.example.tracker_presentation.tracker_overview.component.TrackedFoodItem

@OptIn(ExperimentalCoilApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TrackerOverviewScreen(
    onNavigateToSearch: (String, Int, Int, Int) -> Unit,
    viewModel: TrackerOverviewViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val state = viewModel.state
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = spacing.spaceMedium)
    ) {
        item {
            NutrientsHeader(state = state)

            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            DaySelector(
                date = state.date,
                onPreviousDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick)
                },
                onNextDayClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnNextDayClick)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium)
            )

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }

        items(state.meals) { meal ->
            ExpandableMeal(
                meal = meal,
                onToggleClick = {
                    viewModel.onEvent(TrackerOverviewEvent.OnToggleMealClick(meal))
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = spacing.spaceSmall)
                    ) {
                        val foods = state.trackedFoods.filter {
                            it.mealType == meal.mealType
                        }

                        foods.forEach { food ->
                            TrackedFoodItem(
                                trackedFood = food,
                                onDeleteClick = {
                                    viewModel.onEvent(
                                        TrackerOverviewEvent
                                            .OnDeleteTrackedFoodClick(food)
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(spacing.spaceMedium))
                        }
                        AddButton(
                            text = stringResource(
                                id = R.string.add_meal,
                                meal.name.asString(context)
                            ),
                            onClick = {
                                onNavigateToSearch(
                                    meal.name.asString(context),
                                    state.date.dayOfMonth,
                                    state.date.monthValue,
                                    state.date.year
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}