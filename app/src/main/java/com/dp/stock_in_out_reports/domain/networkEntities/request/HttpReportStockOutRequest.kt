package com.dp.stock_in_out_reports.domain.networkEntities.request

import com.google.gson.annotations.SerializedName

class HttpReportStockOutRequest {
    @SerializedName("selectedDate")
    var selectedDate: String? = null
}