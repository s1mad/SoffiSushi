@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.soffisushi.presentation.component.top_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.soffisushi.R
import com.example.soffisushi.navigation.Screen
import com.example.soffisushi.presentation.component.ProductDetailsDialog
import com.example.soffisushi.presentation.viewmodel.SoffiSushiViewModel
import com.example.soffisushi.util.SearchFilter
import com.example.soffisushi.util.Status
import com.example.soffisushi.util.filter

@Composable
fun TopBar(viewModel: SoffiSushiViewModel, navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isSearching = rememberSaveable { mutableStateOf(false) }

    val searchText = rememberSaveable { mutableStateOf("") }

    val focusRequester = FocusRequester()

    LaunchedEffect(isSearching.value) {
        if (isSearching.value) {
            focusRequester.requestFocus()
        } else {
            searchText.value = ""
        }
    }

    val topBarHeight = remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { topBarHeight.value.toDp() })
            .background(MaterialTheme.colorScheme.surface)
    )

    SearchBar(
        query = searchText.value,
        onQueryChange = { searchText.value = it },
        onSearch = {
            viewModel.updateTopBarSearchRequestText(searchText.value.trim())
            navController.navigate(Screen.Products.routeWithArg(SearchFilter(searchRequest = searchText.value.trim())))
            searchText.value = ""
            isSearching.value = false
        },
        active = isSearching.value,
        onActiveChange = { isSearching.value = it },
        placeholder = {
            if (isSearching.value) {
                Text(text = stringResource(R.string.search))
            } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (currentDestination?.route == Screen.Products.routeAndArg()) {
                        viewModel.topBarSearchRequestText.value
                    } else if (currentDestination?.route == Screen.Categories.routeAndArg()
                        && viewModel.selectedCategory.value != null
                    ) {
                        viewModel.selectedCategory.value!!.name
                    } else {
                        stringResource(R.string.app_name)
                    },
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        },
        shape = RectangleShape,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onGloballyPositioned { coordinates ->
                topBarHeight.value = with(coordinates) {
                    positionInWindow().y.toInt() + size.height
                }
            },
        tonalElevation = 0.dp,
        leadingIcon = {
            if (isSearching.value) {
                if (searchText.value.isEmpty()) {
                    IconButton(onClick = { isSearching.value = false }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "ArrowBack"
                        )
                    }
                } else {
                    IconButton(onClick = { searchText.value = "" }) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "ArrowBack"
                        )
                    }
                }
            } else {
                if (currentDestination?.route == Screen.Products.route ||
                    currentDestination?.route == Screen.Categories.route ||
                    currentDestination?.route == Screen.Cart.route ||
                    currentDestination?.route == Screen.CartSuccessOrErrorOrder.route ||
                    currentDestination?.route == Screen.Stocks.route ||
                    currentDestination?.route == Screen.AboutUs.route
                ) {
                    IconButton(onClick = { }, enabled = false) { }
                } else {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "ArrowBAck"
                        )
                    }
                }
            }
        },
        trailingIcon = {
            IconButton(onClick = {
                if (isSearching.value) {
                    viewModel.updateTopBarSearchRequestText(searchText.value.trim())
                    navController.navigate(Screen.Products.routeWithArg(SearchFilter(searchRequest = searchText.value.trim())))
                    searchText.value = ""
                    isSearching.value = false
                } else {
                    isSearching.value = true
                }
            }) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search")
            }
        },
    ) {

        if (searchText.value.isBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = stringResource(R.string.type_search_request),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            val products = (if (viewModel.products.value is Status.Success)
                (viewModel.products.value as Status.Success).data
            else emptyList()).filter(
                SearchFilter(searchRequest = searchText.value.trim()),
                categories = if (viewModel.categories.value is Status.Success) {
                    (viewModel.categories.value as Status.Success).data
                } else {
                    emptyList()
                }
            )
            if (products.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(48.dp))
                    Text(
                        text = stringResource(R.string.not_found),
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(products) { product ->
                        SearchBarItem(product = product, viewModel = viewModel)
                    }
                }
                ProductDetailsDialog(
                    product = viewModel.productToDetails,
                    viewModel = viewModel
                )
            }
        }
    }
}
