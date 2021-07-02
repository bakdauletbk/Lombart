package kz.pillikan.lombart.common.views

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.button.MaterialButton
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.helpers.formatDate
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.request.home.PayRequest
import kz.pillikan.lombart.content.model.response.home.CardListResponse
import kz.pillikan.lombart.content.model.response.home.Tickets
import kz.pillikan.lombart.content.view.home.HomeFragment
import kz.pillikan.lombart.content.view.notifications.NotificationsFragment
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick


open class BaseFragment : Fragment() {

    var alert: Dialog? = null

    private var cardId: String? = null

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

    fun showAlertDialog(
        context: Context,
        message: String
    ) {
        val builder = MaterialDialog.Builder(context)
            .title(message)
            .dividerColor(context.resources.getColor(R.color.green))
            .positiveText("OK")
            .positiveColorRes(R.color.green)
        builder.cancelable(false)
        builder.onAny { dialog: MaterialDialog?, which: DialogAction ->
            if (which == DialogAction.POSITIVE) {
                val appPackageName =
                    context.packageName
                try {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Constants.URI_PLAY_MARKET + appPackageName)
                        )
                    )
                    (context as Activity).finish()
                } catch (e: ActivityNotFoundException) {
                    context.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Constants.URI_APP + appPackageName)
                        )
                    )
                    (context as Activity).finish()
                }
            }
        }

        builder.show()
    }

    @SuppressLint("SetTextI18n")
    fun showCustomAlert(
        type: Int,
        loansList: Tickets,
        cardListResponse: CardListResponse,
        callback: HomeFragment
    ) {
        var isPay = false
        alert = Dialog(requireContext())
        alert!!.requestWindowFeature(Window.FEATURE_NO_TITLE)

        alert!!.setContentView(R.layout.alert_dialog_pay)
        val tvId: TextView = alert!!.findViewById(R.id.tv_id)
        val tvLoans: TextView = alert!!.findViewById(R.id.tv_loans)
        val tvAmount: TextView = alert!!.findViewById(R.id.tv_renewal_amount)
        val tvDate: TextView = alert!!.findViewById(R.id.tv_date)
        val tvTotalPrice: TextView = alert!!.findViewById(R.id.tv_loan_amount)
        val btnPay: MaterialButton = alert!!.findViewById(R.id.btn_pay_loan)
        val ivClose: ImageView = alert!!.findViewById(R.id.iv_close)

        val spCards: Spinner = alert!!.findViewById(R.id.sp_cards)

        initSpinner(cardListResponse, spCards, callback)

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
                    val payRequest = PayRequest(
                        ticket = base64encode(transaction.toString()),
                        amount = base64encode(price.toString()),
                        card_id = cardId?.let { it1 -> base64encode(it1) }
                    )
                    callback.payLoans(payRequest)
                    alert!!.dismiss()
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

    private fun initSpinner(
        cardListResponse: CardListResponse,
        spinner: Spinner,
        callback: HomeFragment
    ) {
        val cardList = mutableListOf<String>()

        for (i in 0 until cardListResponse.cards.size) {
            cardList.add(cardListResponse.cards[i].card_hash.toString())
        }

        ArrayAdapter(requireContext(), R.layout.item_card_spinner, cardList).also { adapter ->
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                cardId = cardListResponse.cards[position].id
            }
        }
    }

}