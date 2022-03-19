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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dp.stock_in_out_reports.R
import com.dp.stock_in_out_reports.databinding.RecyclerReportStockOutBinding
import com.dp.stock_in_out_reports.domain.localEntities.DateRequest
import com.dp.stock_in_out_reports.domain.networkEntities.request.HttpReportStockOutRequest
import com.dp.stock_in_out_reports.ui.listener.DateTimeStrategy
import com.dp.stock_in_out_reports.ui.listener.SafeClickListener
import com.dp.stock_in_out_reports.ui.viewmodel.ReportStockOutViewModel
import java.util.*
import androidx.lifecycle.Observer
import com.dp.stock_in_out_reports.ui.adapter.ReportStockOutAdapter

class ReportStockOutActivity : AppCompatActivity() {

    lateinit var currentTime: Calendar

    val DAILY = 0
    val WEEKLY = 1
    val MONTHLY = 2
    val YEARLY = 3

    private lateinit var binding: RecyclerReportStockOutBinding
    private lateinit var vModel: ReportStockOutViewModel
    private var TAG = "MainActivity: "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(
                this@ReportStockOutActivity,
                R.layout.recycler_report_stock_out
            )
        vModel = ViewModelProvider(this).get(ReportStockOutViewModel::class.java)
        binding.reportStockOutViewModel = vModel
        binding.lifecycleOwner = this@ReportStockOutActivity
        supportActionBar?.hide()
        binding.reportStockOutProgBar.visibility = View.GONE
        DateTimeStrategy()
        initUI()

    }
    private fun initUI() {

        binding.apply {
            currentTime = Calendar.getInstance()
            var datePicker: DatePickerDialog = DatePickerDialog(
                this@ReportStockOutActivity,
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
                this@ReportStockOutActivity,
                R.array.stockOutDaily, android.R.layout.simple_spinner_item
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner3.adapter = adapter
            spinner3.setSelection(0)
            spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
            val period = spinner3.selectedItemPosition

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
                    reportStockOutProgBar.visibility = View.VISIBLE
                    var mapRequest = HttpReportStockOutRequest()
                    mapRequest.selectedDate = request.daily

                    getReportStockOut(mapRequest)
                }

            }
        }
    }

    fun getReportStockOut(mapRequest: HttpReportStockOutRequest) {
        vModel.getReportStockOut(this, mapRequest).observe(this, Observer { it

            var statusCode = it.statusCode

            if (statusCode == 200) {

                binding.apply {
                    binding.reportStockOutProgBar.visibility = View.GONE
                    reportStockOutRecyclerView.layoutManager =
                        GridLayoutManager(
                            this@ReportStockOutActivity,
                            2,
                            GridLayoutManager.VERTICAL,
                            false
                        )
                    reportStockOutRecyclerView.layoutManager =
                        reportStockOutRecyclerView.layoutManager

                    reportStockOutRecyclerView.adapter = it.reportStockOutList?.let { it1 ->
                        ReportStockOutAdapter(
                            it1
                        )
                    }
                }
            } else {
                binding.reportStockOutProgBar.visibility = View.GONE
                Toast.makeText(
                    this,
                    "Default: ${statusCode} ${it.message} NOT FOUND!",
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


            val period = spinner3.selectedItemPosition
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