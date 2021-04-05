package kz.pillikan.lombart.common.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.formatDate
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.response.home.Tickets
import kz.pillikan.lombart.content.view.notifications.NotificationsFragment
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

open class BaseFragment : Fragment() {

    private var alert: Dialog? = null

    fun errorDialog(errorMsg: String) {
        activity?.alert {
            title = getString(R.string.error_unknown_title)
            message = errorMsg
            isCancelable = false
            positiveButton(getString(R.string.dialog_ok)) { dialog ->
                dialog.dismiss()
            }
        }?.show()
    }

    @SuppressLint("SetTextI18n")
    fun showCustomAlert(type: Int, loansList: Tickets) {

        val intent = Intent()
        var isPay = false
        alert = Dialog(requireContext())
        alert!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        when (type) {
            Constants.ALERT_TYPE_LOAN_DETAILS -> {
                alert!!.setContentView(R.layout.alert_dialog_pay)
                val tvId: TextView = alert!!.findViewById(R.id.tv_id)
                val tvLoans: TextView = alert!!.findViewById(R.id.tv_loans)
                val tvAmount: TextView = alert!!.findViewById(R.id.tv_renewal_amount)
                val tvDate: TextView = alert!!.findViewById(R.id.tv_date)
                val tvTotalPrice: TextView = alert!!.findViewById(R.id.tv_loan_amount)
                val btnPay: MaterialButton = alert!!.findViewById(R.id.btn_pay_loan)
                val ivClose: ImageView = alert!!.findViewById(R.id.iv_close)

                var specification: String = Constants.EMPTY
                for (i in 0 until loansList.items.size) {
                    specification += "\n${loansList.items[i].Specification}\n"
                }

                val price = loansList.ticketInfo.TotalPayment
                val date = formatDate(loansList.ticketInfo.WaitDate!!)
                val transaction = loansList.ticketInfo.Number

                tvLoans.text = specification
                tvId.text = Constants.NUMBERING + transaction
                tvAmount.text = price + Constants.MONEY
                tvDate.text = date + Constants.YEARS
                tvTotalPrice.text = loansList.ticketInfo.totalDebt + Constants.MONEY

                ivClose.onClick {
                    alert!!.dismiss()
                }

                btnPay.onClick {
                    when (isPay) {
                        true -> {
                            isPay = false
                            alert!!.dismiss()
                            showCustomAlert(Constants.ALERT_TYPE_SUCCESS, loansList)
                        }
                        false -> {
                            isPay = true
                            btnPay.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.orange
                                )
                            )
                        }
                    }
                }
            }

            Constants.ALERT_TYPE_SUCCESS -> {
                alert!!.setContentView(R.layout.alert_dialog_successfully)
                val tvSum: TextView = alert!!.findViewById(R.id.tv_sum)
                val tvDate: TextView = alert!!.findViewById(R.id.tv_date)
                val tvTransaction: TextView = alert!!.findViewById(R.id.tv_number_transaction)
                val btnShare: MaterialButton = alert!!.findViewById(R.id.btn_to_share)
                val ivClose: ImageView = alert!!.findViewById(R.id.iv_close)

                tvSum.text = loansList.ticketInfo.TotalPayment + Constants.MONEY
                tvDate.text = formatDate(loansList.ticketInfo.WaitDate!!) + Constants.YEARS
                tvTransaction.text = loansList.ticketInfo.Number

                val textShare =
                    Constants.NUMBER_TRANSACTION + loansList.ticketInfo.Number + Constants.PRICE + loansList.ticketInfo.TotalPayment + Constants.MONEY + Constants.DATE_TRANSACTION + formatDate(
                        loansList.ticketInfo.WaitDate
                    ) + Constants.YEARS + Constants.STATUS_PAY

                btnShare.onClick {
                    intent.action = Intent.ACTION_SEND
                    intent.type = NotificationsFragment.TEXT_TYPE
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        textShare
                    )
                    startActivity(Intent.createChooser(intent, NotificationsFragment.SHARE))
                }

                ivClose.onClick {
                    alert!!.dismiss()
                }
            }
        }

        alert!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alert!!.show()
    }

    fun call(phone: String) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                )
            ) {
                activity?.runOnUiThread {
                    Toast.makeText(
                        activity?.applicationContext,
                        getString(R.string.call),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42
                )
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse(Constants.TEL + phone)
                    startActivity(intent)
                }
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    42
                )
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CALL_PHONE
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    val intent = Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse(Constants.TEL + phone)
                    startActivity(intent)
                }
            }
        } else {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse(Constants.TEL + phone)
            startActivity(intent)
            // Permission has already been granted
        }
    }


}