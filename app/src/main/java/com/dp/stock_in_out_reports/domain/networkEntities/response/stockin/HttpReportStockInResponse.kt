package com.dp.stock_in_out_reports.domain.networkEntities.response.stockin


import com.google.gson.annotations.SerializedName

data class HttpReportStockInResponse(
    @SerializedName("message")
    var message: String? = null,
    @SerializedName("reportStockInList")
    var reportStockInList: List<ReportStockIn>? = null,
    @SerializedName("resultMessage")
    var resultMessage: String? = null,
    @SerializedName("statusCode")
    var statusCode: Int? = null,
    @SerializedName("timeStamp")
    var timeStamp: String? = null
)