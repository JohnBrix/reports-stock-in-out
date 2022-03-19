package com.dp.stock_in_out_reports.domain.networkEntities.response.stockout


import com.google.gson.annotations.SerializedName

data class HttpReportStockOutResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("reportStockOutList")
    var reportStockOutList: List<ReportStockOut>? = null,
    @SerializedName("resultMessage")
    var resultMessage: String? = null,
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("timeStamp")
    var timeStamp: String? = null
)