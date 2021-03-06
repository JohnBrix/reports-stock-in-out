package com.dp.stock_in_out_reports.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dp.stock_in_out_reports.data.RetrofitClient
import com.dp.stock_in_out_reports.data.constant.ReportsStocks
import com.dp.stock_in_out_reports.domain.networkEntities.response.stockin.HttpReportStockInResponse
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockInRequest
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockOutRequest
import com.dp.stock_in_out_reports.domain.networkEntities.response.stockout.HttpReportStockOutResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportsStocksRepository {
    private var retrofit = RetrofitClient()
    private val TAG = "ReportsStocksRepository: "

    fun getReportsStockOut(context: Context, request: HttpReportStockOutRequest)
            : LiveData<HttpReportStockOutResponse> {
        var liveData = MutableLiveData<HttpReportStockOutResponse>()

        val userService: ReportsStocks = retrofit.getReportStockOut(context)
            .create(ReportsStocks::class.java)
        var call: Call<HttpReportStockOutResponse> =
            userService.getReportStockOut(request)

        call.enqueue(object : Callback<HttpReportStockOutResponse?> {
            override fun onResponse(
                call: Call<HttpReportStockOutResponse?>,
                response: Response<HttpReportStockOutResponse?>
            ) {

                if (response.code() == 200) {
                    Log.i(TAG, "${response.body()}")
                    liveData.value = response.body()
                } else {
                    var product = HttpReportStockOutResponse()
                    var gson = Gson();
                    var adapter = gson.getAdapter(HttpReportStockOutResponse::class.java)

                    if (response.errorBody() != null)
                        product = adapter.fromJson(response.errorBody()?.string())

                    Log.i(TAG, "${product}")
                    liveData.value = product
                }

            }

            override fun onFailure(call: Call<HttpReportStockOutResponse?>, t: Throwable) {
                Log.e("OnError: ", "${call} & ${t.message}")
                var prod = HttpReportStockOutResponse()
                var httpProd = HttpReportStockOutResponse()
                httpProd.resultMessage = "NO_CONTENT"
                Log.i(TAG, "${prod}")
                liveData.value = prod
            }

        })
        Log.i(TAG, "${liveData.value}")
        return liveData
    }

    fun getReportsStockIn(context: Context, request: HttpReportStockInRequest)
            : LiveData<HttpReportStockInResponse> {
        var liveData = MutableLiveData<HttpReportStockInResponse>()

        val userService: ReportsStocks = retrofit.getReportStockIn(context)
            .create(ReportsStocks::class.java)
        var call: Call<HttpReportStockInResponse> =
            userService.getReportStockIn(request)

        call.enqueue(object : Callback<HttpReportStockInResponse?> {
            override fun onResponse(
                call: Call<HttpReportStockInResponse?>,
                response: Response<HttpReportStockInResponse?>
            ) {

                if (response.code() == 200) {
                    Log.i(TAG, "${response.body()}")
                    liveData.value = response.body()
                } else {
                    var product = HttpReportStockInResponse()
                    var gson = Gson();
                    var adapter = gson.getAdapter(HttpReportStockInResponse::class.java)

                    if (response.errorBody() != null)
                        product = adapter.fromJson(response.errorBody()?.string())

                    Log.i(TAG, "${product}")
                    liveData.value = product
                }

            }

            override fun onFailure(call: Call<HttpReportStockInResponse?>, t: Throwable) {
                Log.e("OnError: ", "${call} & ${t.message}")
                var prod = HttpReportStockInResponse()
                var httpProd = HttpReportStockInResponse()
                httpProd.resultMessage = "NO_CONTENT"
                Log.i(TAG, "${prod}")
                liveData.value = prod
            }

        })
        Log.i(TAG, "${liveData.value}")
        return liveData
    }
}