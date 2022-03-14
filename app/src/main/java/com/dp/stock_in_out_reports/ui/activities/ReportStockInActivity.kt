package com.dp.stock_in_out_reports.ui.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dp.stock_in_out_reports.R
import com.dp.stock_in_out_reports.databinding.RecyclerReportStockInBinding
import com.dp.stock_in_out_reports.domain.localEntities.DateRequest
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockInRequest
import com.dp.stock_in_out_reports.ui.adapter.ReportStockInAdapter
import com.dp.stock_in_out_reports.ui.listener.DateTimeStrategy
import com.dp.stock_in_out_reports.ui.listener.SafeClickListener
import com.dp.stock_in_out_reports.ui.viewmodel.ReportStockInViewModel
import java.util.*

class ReportStockInActivity : AppCompatActivity() {


    lateinit var currentTime: Calendar

    val DAILY = 0
    val WEEKLY = 1
    val MONTHLY = 2
    val YEARLY = 3

    private lateinit var binding: RecyclerReportStockInBinding
    private lateinit var vModel: ReportStockInViewModel
    private var TAG = "MainActivity: "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(
                this@ReportStockInActivity,
                R.layout.recycler_report_stock_in
            )
        vModel = ViewModelProvider(this).get(ReportStockInViewModel::class.java)
        binding.reportStockInViewModel = vModel
        binding.lifecycleOwner = this@ReportStockInActivity
        supportActionBar?.hide()
        DateTimeStrategy()
        initUI()

    }

    private fun initUI() {

        binding.apply {
            currentTime = Calendar.getInstance()
            var datePicker: DatePickerDialog = DatePickerDialog(
                this@ReportStockInActivity,
                { view, y, m, d ->
                    currentTime.set(Calendar.YEAR, y)
                    currentTime.set(Calendar.MONTH, m)
                    currentTime.set(Calendar.DAY_OF_MONTH, d)
                    update()
                }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH), currentTime.get(
                    Calendar.DAY_OF_MONTH
                )
            )

            val adapter = ArrayAdapter.createFromResource(
                this@ReportStockInActivity,
                R.array.stockInDaily, android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner2.adapter = adapter
            spinner2.setSelection(0)
            spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    pos: Int,
                    id: Long
                ) {
                    update()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            currentBox.setOnClickListener { datePicker.show() }
            previousButton.setOnClickListener { addDate(-1) }
            nextButton!!.setOnClickListener { addDate(1) }
        }
    }

    fun update() {
        binding.apply {
            val period = spinner2.selectedItemPosition

            val cTime = currentTime.clone() as Calendar
            var eTime = currentTime.clone() as Calendar
            var d = DateTimeStrategy()

            if (period == DAILY) {
                currentBox.text =
                    " [" + d.getSQLDateFormat(currentTime) + "] "
                currentBox.textSize = 16f

                Toast.makeText(
                    applicationContext,
                    "DAILY ${d.getSQLDateFormat(currentTime)}",
                    Toast.LENGTH_SHORT
                )
                    .show()

                var request = DateRequest()
                request.daily = d.getSQLDateFormat(currentTime)

                getSale(request)

            } else if (period == WEEKLY) {
                while (cTime[Calendar.DAY_OF_WEEK] == Calendar.SUNDAY) {
                    cTime.add(Calendar.DATE, -1)
                }

                var toShow = " [" + d.getSQLDateFormat(cTime).toString() + "] ~ ["
                eTime = cTime.clone() as Calendar
                eTime.add(Calendar.DATE, 7)
                toShow += d.getSQLDateFormat(eTime).toString() + "] "
                currentBox.textSize = 16f
                currentBox.text = toShow

                var separate = "${d.getSQLDateFormat(cTime)} and ${d.getSQLDateFormat(eTime)}"
                Toast.makeText(applicationContext, "WEEKLY ${separate}", Toast.LENGTH_SHORT).show()

                var request = DateRequest()
                request.weeklyNow = d.getSQLDateFormat(cTime)
                request.weeklyTo = d.getSQLDateFormat(eTime)

                getSale(request)

            } else if (period == MONTHLY) {
                cTime[Calendar.DATE] = 1
                eTime = cTime.clone() as Calendar
                eTime.add(Calendar.MONTH, 1)
                eTime.add(Calendar.DATE, -1)
                currentBox.textSize = 18f
                currentBox.text =
                    " [" + currentTime!![Calendar.YEAR] + "-" + (currentTime!![Calendar.MONTH] + 1) + "] "

                var separate =
                    "${currentTime!![Calendar.YEAR]} and ${(currentTime!![Calendar.MONTH] + 1)}"
                Toast.makeText(applicationContext, "MONTHLY ${separate}", Toast.LENGTH_SHORT).show()

                var request = DateRequest()
                request.monthlyNumber = (currentTime!![Calendar.MONTH] + 1).toString()
                request.monthlyYear = currentTime!![Calendar.YEAR].toString()

                getSale(request)

            } else if (period == YEARLY) {
                cTime[Calendar.DATE] = 1
                cTime[Calendar.MONTH] = 0
                eTime = cTime.clone() as Calendar
                eTime.add(Calendar.YEAR, 1)
                eTime.add(Calendar.DATE, -1)
                currentBox!!.textSize = 20f
                currentBox!!.text = " [" + currentTime!![Calendar.YEAR] + "] "
                Toast.makeText(
                    applicationContext,
                    "YEARLY ${currentBox.text.toString()}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                var request = DateRequest()
                request.yearly = currentTime!![Calendar.YEAR].toString()

                getSale(request)
            }
            currentTime = cTime
            var total = 0.0

        }
    }

    fun getSale(request: DateRequest) {

        binding.apply {
            submit.setSafeOnClickListener {
                Log.i(TAG, request.toString())

                Toast.makeText(
                    applicationContext,
                    "REQUEST $request",
                    Toast.LENGTH_SHORT
                )
                    .show()

                if (request.daily?.isNotEmpty() == true) {
                    reportStockInProgBar.visibility = View.VISIBLE
                    var mapRequest = HttpReportStockInRequest()
                    mapRequest.selectedDate = request.daily

                    getReportStockIn(mapRequest)
                }

            }
        }
    }

    fun getReportStockIn(mapRequest: HttpReportStockInRequest) {
        vModel.getReportStockIn(this, mapRequest).observe(this, Observer {
            it

            var statusCode = it.statusCode

            if (statusCode == 200) {
                binding.apply {
                    reportStockInRecyclerView.layoutManager =
                        GridLayoutManager(
                            this@ReportStockInActivity,
                            2,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                    reportStockInRecyclerView.layoutManager =
                        reportStockInRecyclerView.layoutManager

                    reportStockInRecyclerView.adapter = it.reportStockInList?.let { it1 ->
                        ReportStockInAdapter(
                            it1
                        )
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Default: ${statusCode} ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        )
    }

    /**
     * Add date.
     * @param increment
     */
    private fun addDate(increment: Int) {
        binding.apply {


            val period = spinner2.selectedItemPosition
            if (period == DAILY) {
                currentTime.add(Calendar.DATE, 1 * increment)
            }/* else if (period == WEEKLY) {
                currentTime.add(Calendar.DATE, 7 * increment)
            } else if (period == MONTHLY) {
                currentTime.add(Calendar.MONTH, 1 * increment)
            } else if (period == YEARLY) {
                currentTime.add(Calendar.YEAR, 1 * increment)
            }*/
            update()
        }
    }

    fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }
}