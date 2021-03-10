package kz.pillikan.lombart.common.views.utils

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.response.home.FinenessPriceResponse
import kz.pillikan.lombart.content.view.home.HomeFragment
import org.jetbrains.anko.sdk27.coroutines.onClick

open class СalculatorPrice {

    private var callback: HomeFragment

    constructor(callback: HomeFragment) : super() {
        this.callback = callback
    }

    @SuppressLint("SetTextI18n")
    fun setFinenessPrice(finenessPrice: FinenessPriceResponse) {
        val tvFinenessName = callback.view?.findViewById<TextView>(R.id.tv_fineness_name1)
        val tvFinenessPrice = callback.view?.findViewById<TextView>(R.id.tv_fineness_price1)

        val tvFinenessName2 = callback.view?.findViewById<TextView>(R.id.tv_fineness_name2)
        val tvFinenessPrice2 = callback.view?.findViewById<TextView>(R.id.tv_fineness_price2)

        val tvFinenessName3 = callback.view?.findViewById<TextView>(R.id.tv_fineness_name3)
        val tvFinenessPrice3 = callback.view?.findViewById<TextView>(R.id.tv_fineness_price3)

        for (i in 0 until finenessPrice.prices.size) {
            when (i) {
                Constants.FINENESS_FIRST -> {
                    tvFinenessName?.text =
                        Constants.FINENESS + finenessPrice.prices[Constants.FINENESS_FIRST].title
                    tvFinenessPrice?.text =
                        finenessPrice.prices[Constants.FINENESS_FIRST].value + Constants.MONEY
                }
                Constants.FINENESS_SECOND -> {
                    tvFinenessName2?.text =
                        Constants.FINENESS + finenessPrice.prices[Constants.FINENESS_SECOND].title
                    tvFinenessPrice2?.text =
                        finenessPrice.prices[Constants.FINENESS_SECOND].value + Constants.MONEY
                }
                Constants.FINENESS_THIRD -> {
                    tvFinenessName3?.text =
                        Constants.FINENESS + finenessPrice.prices[Constants.FINENESS_THIRD].title
                    tvFinenessPrice3?.text =
                        finenessPrice.prices[Constants.FINENESS_THIRD].value + Constants.MONEY
                }
            }
        }
    }

    fun setLoanAmount(finenessPrice: FinenessPriceResponse) {

        val llFineness = callback.view?.findViewById<LinearLayout>(R.id.ll_fineness1)
        val llFineness2 = callback.view?.findViewById<LinearLayout>(R.id.ll_fineness2)
        val llFineness3 = callback.view?.findViewById<LinearLayout>(R.id.ll_fineness3)

        setPrice(
            finenessPrice.prices[Constants.FINENESS_FIRST].value!!.toLong(),
            percent1 = finenessPrice.percent1!!,
            percent2 = finenessPrice.percent2!!,
            limit = finenessPrice.amount_limit
        )
        llFineness?.onClick {
            setEnable(Constants.FINENESS_FIRST, finenessPrice)
        }
        llFineness2?.onClick {
            setEnable(Constants.FINENESS_SECOND, finenessPrice)
        }
        llFineness3?.onClick {
            setEnable(Constants.FINENESS_THIRD, finenessPrice)
        }
    }

    private fun setEnable(position: Int, finenessPrice: FinenessPriceResponse) {
        val spinner = callback.view?.findViewById<Spinner>(R.id.spinner_fineness)
        val llFineness = callback.view?.findViewById<LinearLayout>(R.id.ll_fineness1)
        val llFineness2 = callback.view?.findViewById<LinearLayout>(R.id.ll_fineness2)
        val llFineness3 = callback.view?.findViewById<LinearLayout>(R.id.ll_fineness3)

        setPrice(
            finenessPrice.prices[position].value!!.toLong(),
            percent1 = finenessPrice.percent1!!,
            percent2 = finenessPrice.percent2!!,
            limit = finenessPrice.amount_limit
        )
        spinner?.setSelection(position)
        when (position) {
            Constants.FINENESS_FIRST -> {
                llFineness?.isEnabled = false
                llFineness2?.isEnabled = true
                llFineness3?.isEnabled = true
            }
            Constants.FINENESS_SECOND -> {
                llFineness2?.isEnabled = false
                llFineness?.isEnabled = true
                llFineness3?.isEnabled = true
            }
            Constants.FINENESS_THIRD -> {
                llFineness3?.isEnabled = false
                llFineness?.isEnabled = true
                llFineness2?.isEnabled = true
            }
        }
    }

    fun setSpinner(finenessPrice: FinenessPriceResponse) {
        val finenessList = mutableListOf<String>()
        val spinner = callback.view?.findViewById<Spinner>(R.id.spinner_fineness)

        for (i in Constants.ZERO until finenessPrice.prices.size) {
            finenessList.add(finenessPrice.prices[i].title!!)
        }

        callback.context?.let {
            ArrayAdapter(
                it,
                R.layout.item_fineness_spinner,
                finenessList
            ).also { adapter ->
                spinner?.adapter = adapter
            }
        }

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setPrice(
                    finenessPrice.prices[position].value!!.toLong(),
                    percent1 = finenessPrice.percent1!!,
                    percent2 = finenessPrice.percent2!!,
                    limit = finenessPrice.amount_limit
                )
                setEnable(position, finenessPrice)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPrice(price: Long, percent1: String, percent2: String, limit: Long) {
        val etDay = callback.view?.findViewById<EditText>(R.id.et_day)
        val etGram = callback.view?.findViewById<EditText>(R.id.et_gram)
        val tvPrice = callback.view?.findViewById<TextView>(R.id.tv_refundable_amount)
        when (etDay?.text!!.isNotEmpty() && etGram?.text!!.isNotEmpty()) {
            true -> {
                val day = etDay.text.toString().toInt()
                setValidateText(price)
                val gram = etDay.text.toString().toFloat()
                val result = (((price * gram) / Constants.DAYS) * day).toLong()
                when (result >= limit) {
                    true -> {
                        val percent = result * percent2.toFloat()
                        tvPrice?.text = percent.toLong().toString() + Constants.MONEY
                    }
                    false -> {
                        val percent = result * percent1.toFloat()
                        tvPrice?.text = percent.toLong().toString() + Constants.MONEY
                    }
                }
            }
            false -> {
                Snackbar.make(callback.requireView(), "Введите данные!", Snackbar.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setValidateText(price: Long) {
        val etDay = callback.view?.findViewById<EditText>(R.id.et_day)
        val etGram = callback.view?.findViewById<EditText>(R.id.et_gram)
        val tvPrice = callback.view?.findViewById<TextView>(R.id.tv_refundable_amount)
        etDay?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                when (etDay.text.isNotEmpty() && etGram?.text!!.isNotEmpty()) {
                    true -> {
                        val gram = etGram?.text.toString().toFloat()
                        val result =
                            ((price * gram) / Constants.DAYS) * etDay.text.toString().toInt()
                        tvPrice?.text = result.toLong().toString() + Constants.MONEY
                    }
                    false -> {
                        etDay.error = "Введенный вами день некорректно!"
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })
        etGram?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                when (etGram.text.isNotEmpty() && etDay?.text!!.isNotEmpty()) {
                    true -> {
                        val gram = etGram.text.toString().toFloat()
                        val result =
                            ((price * gram) / Constants.DAYS) * etDay?.text.toString().toInt()
                        tvPrice?.text = result.toLong().toString() + Constants.MONEY
                    }
                    false -> {
                        etGram.error = "Введенный вами грамм некорректно!"
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {
            }
        })
    }

}