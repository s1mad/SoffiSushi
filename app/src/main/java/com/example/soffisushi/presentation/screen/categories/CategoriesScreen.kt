package com.example.soffisushi.presentation.screen.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.soffisushi.R
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.component.CircularProgressBox
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.presentation.screen.categories.component.CategoryItem
import com.example.soffisushi.presentation.screen.products.ProductsScreen
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.SearchFilter
import com.example.soffisushi.util.Status

@Composable
fun CategoriesScreen(
    viewModel: SoffiSushiViewModel,
    navController: NavHostController,
    selectedCategoryId: Long? = null,
    modifier: Modifier
) {
    val resultCategories = remember { viewModel.categories }

    when (resultCategories.value) {
        is Status.Pending -> {
            if (viewModel.selectedPoint.value is Status.Success) {
                viewModel.getCategories()
            } else {
                CircularProgressBox(modifier = modifier)
            }
        }

        is Status.Loading -> {
            CircularProgressBox(modifier = modifier)
        }

        is Status.Error -> {
            val exception = (resultCategories.value as Status.Error).exception

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = exception.message.toString(),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
                WrapContentButton(
                    text = stringResource(R.string.try_again),
                    onClick = {
                        viewModel.getCategories()
                    }
                )
            }
        }

        is Status.Success -> {
            val categories =
                (resultCategories.value as Status.Success).data.sortedBy { it.sortNumber }

            if (categories.isNotEmpty()) {
                val filteredCategory = categories.filter { it.parentId == selectedCategoryId }
                viewModel.selectedCategory.value =
                    categories.find { it.id == selectedCategoryId }

                if (filteredCategory.isEmpty() && selectedCategoryId != null) {
                    ProductsScreen(
                        viewModel = viewModel,
                        modifier = modifier,
                        searchFilter = SearchFilter(categoryId = selectedCategoryId)
                    )
                } else {
                    LazyVerticalGrid(
                        modifier = modifier,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        columns = GridCells.Adaptive(144.dp)
                    ) {
                        items(filteredCategory) { category ->
                            CategoryItem(
                                category = category,
                                onClick = {
                                    navController.navigate(Screen.Categories.routeWithArg(category.id))
                                }
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = stringResource(R.string.no_categories),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
