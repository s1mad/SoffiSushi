package com.example.soffisushi.util

import com.example.soffisushi.data.remote.firebase.model.Category
import com.example.soffisushi.data.remote.firebase.model.Product

data class SearchFilter(
    val searchRequest: String? = null,
    val categoryId: Long? = null
)

fun List<Product>.filter(
    searchFilter: SearchFilter,
    categories: List<Category>
): List<Product> {
    val filterBySearchRequest = if (searchFilter.searchRequest != null) {
        val productsByName = this
            .filter { product ->
                product.name.contains(
                    searchFilter.searchRequest,
                    ignoreCase = true
                )
            }
        val productsByStructure = this
            .filter { product ->
                product.structure.contains(
                    searchFilter.searchRequest,
                    ignoreCase = true
                )
            }
        val productsByCategory = categories
            .filter { category ->
                category.name.contains(
                    searchFilter.searchRequest,
                    ignoreCase = true
                )
            }
            .flatMap { category ->
                this.filter { it.categoryId == category.id }
            }
        (productsByName + productsByCategory + productsByStructure).distinct()
    } else {
        this
    }
    val filterByCategory = if (searchFilter.categoryId != null) {
        filterBySearchRequest.filter {
            searchFilter.categoryId == it.categoryId
        }
    } else {
        filterBySearchRequest
    }

    return filterByCategory
}
