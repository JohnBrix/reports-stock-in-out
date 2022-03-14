package com.dp.stock_in_out_reports.domain.networkEntities


import com.google.gson.annotations.SerializedName

data class ReportStockIn(
    @SerializedName("dateOfInventory")
    var dateOfInventory: String? = null,
    @SerializedName("product")
    var product: Product? = null,
    @SerializedName("quantity")
    var quantity: Int? = null,
    @SerializedName("supplier")
    var supplier: String? = null,
    @SerializedName("supplierPrice")
    var supplierPrice: Double? = null,
    @SerializedName("transId")
    var transId: Int? = null,
    @SerializedName("transactionNumber")
    var transactionNumber: String? = null
)