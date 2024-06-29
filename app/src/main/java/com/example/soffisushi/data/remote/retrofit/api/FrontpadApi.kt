package com.example.soffisushi.data.remote.retrofit.api

import com.example.soffisushi.data.remote.retrofit.model.ApiResponseNewOrder
import com.example.soffisushi.data.remote.retrofit.util.Constants
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface FrontpadApi {
    @FormUrlEncoded
    @POST(Constants.NEW_ORDER)
    fun newOrder(
        @Field("secret") secret: String,
        @Field("product[]") product: List<Long>,
        @Field("product_kol[]") productKol: List<Int>,
        @Field("product_mod[]") productMod: List<String>?,
        @Field("product_price[]") productPrice: List<Float>?,
        @Field("score") score: Int?,
        @Field("sale") sale: Int?,
        @Field("sale_amount") saleAmount: Float?,
        @Field("card") card: String?,
        @Field("street") street: String?,
        @Field("home") home: String?,
        @Field("pod") pod: String?,
        @Field("et") et: String?,
        @Field("apart") apart: String?,
        @Field("phone") phone: String?,
        @Field("mail") mail: String?,
        @Field("descr") descr: String?,
        @Field("name") name: String?,
        @Field("pay") pay: String?,
        @Field("certificate") certificate: String?,
        @Field("person") person: Int?,
        @Field("tags") tags: List<String>?,
        @Field("hook_status") hookStatus: List<String>?,
        @Field("hook_url") hookUrl: String?,
        @Field("channel") channel: String?,
        @Field("datetime") datetime: String?,
        @Field("affiliate") affiliate: String?,
        @Field("point") point: String?
    ): Call<ApiResponseNewOrder>
}