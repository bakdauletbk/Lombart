package kz.pillikan.lombart.content.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.formatDate
import kz.pillikan.lombart.content.model.response.home.Tickets

class LoansAdapter : RecyclerView.Adapter<LoansAdapter.LoansHolder> {

    private var loansList: ArrayList<Tickets> = ArrayList()

    private var callback: HomeFragment

    constructor(callback: HomeFragment) : super() {
        this.callback = callback
    }

    fun addLoans(loansList: ArrayList<Tickets>) {
        this.loansList.clear()
        this.loansList.addAll(loansList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoansHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.item_loans, parent, false)
        return LoansHolder(root)
    }

    override fun getItemCount(): Int {
        return loansList.size
    }

    override fun onBindViewHolder(holder: LoansAdapter.LoansHolder, position: Int) {
        holder.bind(loansList = loansList[position], callback)
    }

    class LoansHolder(private val root: View) : RecyclerView.ViewHolder(root) {
        private val tvId = root.findViewById(R.id.tv_id) as TextView
        private val tvLoans = root.findViewById(R.id.tv_loans) as TextView
        private val tvPrice = root.findViewById(R.id.tv_price) as TextView
        private val tvDate = root.findViewById(R.id.tv_date) as TextView
        private val btnPay = root.findViewById(R.id.btn_pay) as MaterialButton


        @SuppressLint("SetTextI18n")
        fun bind(loansList: Tickets, callback: HomeFragment) {

            tvLoans.text = loansList.items[0].Specification

            tvId.text = "№ " + loansList.ticketInfo.Number.toString()
            tvPrice.text = loansList.ticketInfo.TotalPayment.toString() + " тг"
            tvDate.text =
                formatDate(loansList.ticketInfo.StartDate!!) + " - " + formatDate(loansList.ticketInfo.EndDate!!)

            btnPay.setOnClickListener { v: View ->
                callback.onPayAlertDialog(loansList)
            }

        }
    }

}