package com.dp.stock_in_out_reports.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dp.stock_in_out_reports.R
import com.dp.stock_in_out_reports.domain.networkEntities.HttpReportStockInResponse
import com.dp.stock_in_out_reports.domain.networkEntities.ReportStockIn
import com.squareup.picasso.Picasso

class ReportStockInAdapter( var item: List<ReportStockIn>)
    : RecyclerView.Adapter<ReportStockInAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImages = itemView.findViewById(R.id.stockInImages) as ImageView
        var itemStockIn = itemView.findViewById(R.id.itemStockIn) as TextView
        var itemModelStockIn = itemView.findViewById(R.id.itemModelStockIn) as TextView
        var transNumberStockIn = itemView.findViewById(R.id.transNumberStockIn) as TextView
        var printNowIn = itemView.findViewById(R.id.printNowIn) as Button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_report_stock_in, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemList = item.get(position)

        Picasso.get().load(itemList?.product?.itemPicture).into(holder.itemImages)
        holder.itemStockIn.text = "ItemName: ${itemList.product?.itemName.toString()}"
        holder.itemModelStockIn.text ="Model: ${itemList.product?.brandModel.toString()}"
        holder.transNumberStockIn.text ="TransN#: ${itemList.transactionNumber.toString()}"

    }

    override fun getItemCount(): Int {
        return item.size;
    }
}