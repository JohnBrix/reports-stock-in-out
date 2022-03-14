package com.dp.stock_in_out_reports.data.constant

import com.dp.stock_in_out_reports.domain.networkEntities.HttpReportStockInResponse
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockInRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ReportsStocks {
    @POST("/report/stockin")
    fun getReportStockIn(@Body request: HttpReportStockInRequest): Call<HttpReportStockInResponse>
}