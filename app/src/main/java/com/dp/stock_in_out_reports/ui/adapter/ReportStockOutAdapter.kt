package com.dp.stock_in_out_reports.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dp.stock_in_out_reports.R
import com.dp.stock_in_out_reports.domain.networkEntities.response.stockout.ReportStockOut
import com.squareup.picasso.Picasso

class ReportStockOutAdapter( var item: List<ReportStockOut>)
    : RecyclerView.Adapter<ReportStockOutAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImages = itemView.findViewById(R.id.stockOutImages) as ImageView
        var itemStockOut = itemView.findViewById(R.id.itemStockOut) as TextView
        var itemModelStockOut = itemView.findViewById(R.id.itemModelStockOut) as TextView
        var transNumberStockOut = itemView.findViewById(R.id.transNumberStockOut) as TextView
        var reasonStockOut = itemView.findViewById(R.id.reasonStockOut) as TextView
        var dateOfTrans = itemView.findViewById(R.id.dateOfTrans) as TextView
        var qty = itemView.findViewById(R.id.qty) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportStockOutAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_report_stock_out, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var itemList = item.get(position)

        Picasso.get().load(itemList?.product?.itemPicture).into(holder.itemImages)
        holder.itemStockOut.text = "ItemName: ${itemList.product?.itemName.toString()}"
        holder.itemModelStockOut.text ="Model: ${itemList.product?.brandModel.toString()}"
        holder.transNumberStockOut.text ="TransN#: ${itemList.transaction.toString()}"
        holder.reasonStockOut.text = "Reason: ${itemList.reason}"
        holder.dateOfTrans.text = "Date Trans#: ${itemList.dateOfTransaction}"
        holder.qty.text = "Quantity: ${itemList.quantity}"
    }

    override fun getItemCount(): Int {
        return item.size;
    }

}