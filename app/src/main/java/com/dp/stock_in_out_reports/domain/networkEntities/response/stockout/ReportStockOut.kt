package com.dp.stock_in_out_reports.domain.networkEntities.response.stockout


import com.google.gson.annotations.SerializedName

data class ReportStockOut(
    @SerializedName("dateOfTransaction")
    var dateOfTransaction: String? = null,
    @SerializedName("product")
    var product: Product? = null,
    @SerializedName("quantity")
    var quantity: Int? = null,
    @SerializedName("reason")
    var reason: String? = null,
    @SerializedName("transId")
    var transId: Int? = null,
    @SerializedName("transaction")
    var transaction: String? = null
)