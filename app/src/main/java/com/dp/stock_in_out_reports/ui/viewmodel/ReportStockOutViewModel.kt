package com.dp.stock_in_out_reports.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockInRequest
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockOutRequest
import com.dp.stock_in_out_reports.domain.networkEntities.response.stockin.HttpReportStockInResponse
import com.dp.stock_in_out_reports.domain.networkEntities.response.stockout.HttpReportStockOutResponse
import com.dp.stock_in_out_reports.repository.ReportsStocksRepository

class ReportStockOutViewModel: ViewModel() {
    val repository: ReportsStocksRepository = ReportsStocksRepository()

    fun getReportStockOut(context: Context, request: HttpReportStockOutRequest): LiveData<HttpReportStockOutResponse> {
        return repository.getReportsStockOut(context,request)
    }
}