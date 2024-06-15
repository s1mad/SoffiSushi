package com.example.soffisushi.domain.model

import com.example.soffisushi.data.remote.firebase.model.Product

data class SingleProduct(
    val id: Long,
    val categoryId: Long,

    val name: String,
    val image: String,
    val price: Int,
    val structure: String,

    val putGingerAndWasabi: Boolean,
    val putSticks: Boolean,

    val new: Boolean,
    val hot: Boolean,
    val stop: Boolean
)

fun Product.toSingleProduct(choice: Int): SingleProduct {
    val index = if (choice < 0) 0 else if (choice >= id.size) id.size - 1 else choice
    return SingleProduct(
        id = id[index],
        categoryId = categoryId,

        name = "$name ${choices[index]}",
        image = image[index],
        price = price[index],
        structure = structure,

        putGingerAndWasabi = putGingerAndWasabi,
        putSticks = putSticks,

        new = new,
        hot = hot,
        stop = stop[index]
    )
}