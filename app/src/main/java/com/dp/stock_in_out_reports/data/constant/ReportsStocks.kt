package com.dp.stock_in_out_reports.data.constant

import com.dp.stock_in_out_reports.domain.networkEntities.response.stockin.HttpReportStockInResponse
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockInRequest
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockOutRequest
import com.dp.stock_in_out_reports.domain.networkEntities.response.stockout.HttpReportStockOutResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportsStocks {
    @POST("/report/stockin")
    fun getReportStockIn(@Body request: HttpReportStockInRequest): Call<HttpReportStockInResponse>
    @POST("/report/stockout")
    fun getReportStockOut(@Body request: HttpReportStockOutRequest): Call<HttpReportStockOutResponse>
}