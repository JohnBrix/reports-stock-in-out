package com.dp.stock_in_out_reports.domain.networkEntities.response.stockout


import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("brandModel")
    var brandModel: String? = null,
    @SerializedName("category")
    var category: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("itemName")
    var itemName: String? = null,
    @SerializedName("itemPicture")
    var itemPicture: String? = null,
    @SerializedName("productId")
    var productId: Int? = null,
    @SerializedName("quantity")
    var quantity: Int? = null,
    @SerializedName("srpPrice")
    var srpPrice: Double? = null,
    @SerializedName("stockIn")
    var stockIn: Any? = null
)