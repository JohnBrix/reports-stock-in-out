package com.dp.stock_in_out_reports.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dp.stock_in_out_reports.domain.networkEntities.response.stockin.HttpReportStockInResponse
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockInRequest
import com.dp.stock_in_out_reports.repository.ReportsStocksRepository

class ReportStockInViewModel: ViewModel() {
    val repository: ReportsStocksRepository = ReportsStocksRepository()

    fun getReportStockIn(context: Context,request: HttpReportStockInRequest):LiveData<HttpReportStockInResponse>{


        return repository.getReportsStockIn(context,request)
    }
}