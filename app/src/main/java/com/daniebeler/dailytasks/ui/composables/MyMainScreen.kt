package com.daniebeler.dailytasks.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.daniebeler.dailytasks.utils.imeAwareInsets
import kotlinx.coroutines.launch

@Composable
fun MyMainScreen(
    viewModel: MainScreenViewModel = hiltViewModel(
        checkNotNull(
            LocalViewModelStoreOwner.current
        ) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        }, "12"
    )
) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState { 2 }

    Scaffold(content = { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .fillMaxSize()
                .imeAwareInsets()
        ) {
            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 1,
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)
            ) { tabIndex ->
                when (tabIndex) {
                    0 -> TodayPage(
                        viewModel,
                        onNavigateToTomorrow = { scope.launch { pagerState.animateScrollToPage(1) } })

                    1 -> TomorrowPage(
                        viewModel,
                        onNavigateToToday = { scope.launch { pagerState.animateScrollToPage(0) } })
                }
            }
        }
    })
}
