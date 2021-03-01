package kz.pillikan.lombart.content.view.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.loadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.convertDpToPixel
import kz.pillikan.lombart.common.helpers.formatDate
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.home.*
import kz.pillikan.lombart.content.viewmodel.home.HomeViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment() {

    private val adapters: LoansAdapter = LoansAdapter(this)
    private lateinit var viewModel: HomeViewModel
    private var alertDialog: Dialog? = null
    private var isDialogVisibility = false
    private val bannerAdapter by lazy { PagerAdapter(context) }

    companion object {
        const val CLEAR_SKY = "01d"
        const val FEW_CLOUDS = "02d"
        const val SCATTERED_CLOUDS = "03d"
        const val BROKEN_CLOUDS = "04d"
        const val SHOWER_RAIN = "09d"
        const val RAIN = "10d"
        const val THUNDERSTORM = "11d"
        const val SNOW = "13d"
        const val MIST = "50d"
        const val USA_CURRENCY = 0
        const val EUROPA_CURRENCY = 1
        const val RUSSIA_CURRENCY = 2
        const val PADDING = 20f
        const val PADDING_TOP = 0
        const val PADDING_BOTTOM = 0
        const val PAGE_MARGIN = 12f
        const val FINENESS_FIRST = 0
        const val FINENESS_SECOND = 1
        const val FINENESS_THIRD = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initViewModel()
        initViewPager()
        initRecyclerView()
        initListeners()
        initUpdateFeed()
        initObservers()
        initAutoScroll()
        initTodayDate()
    }

    private fun initViewPager() {
        vp_banners.adapter = bannerAdapter
        vp_banners.apply {
            setPadding(
                convertDpToPixel(PADDING),
                PADDING_TOP,
                convertDpToPixel(PADDING),
                PADDING_BOTTOM
            )
            pageMargin = convertDpToPixel(PAGE_MARGIN)
            clipToPadding = false
        }
    }

    private fun initRecyclerView() {
        rv_loans.adapter = adapters
        rv_loans.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    private fun initUpdateFeed() {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getLoans()
        }
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getWeather()
        }
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getCurrency()
        }
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getProfile()
        }
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getSliderList()
        }
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getFinenessPrice()
        }
    }

    private fun initListeners() {
        ll_technical_support.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.appealFragment)
            }
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.loanList.observe(viewLifecycleOwner, {
            if (it != null) {
                showIsNotEmpty()
                addLoans(it)
                setLoading(false)
            } else {
                showEmptyLoans()
            }
        })
        viewModel.currencyList.observe(viewLifecycleOwner, {
            if (it != null) {
                setCurrency(it)
                setLoading(false)
            } else {
                errorDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.weatherData.observe(viewLifecycleOwner, {
            if (it != null) {
                setWeather(it)
                setLoading(false)
            } else {
                errorDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.profileInfo.observe(viewLifecycleOwner, {
            if (it != null) {
                setProfile(it)
                setLoading(false)
            } else {
                errorDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.slidersList.observe(viewLifecycleOwner, {
            if (it != null) {
                addSliderList(it)
            } else {
                errorDialog(getString(R.string.error_unknown_body))
                showEmptySlider()
            }
        })
        viewModel.finenessPrice.observe(viewLifecycleOwner, {
            if (it != null) {
                setFinenessPrice(it)
                setSpinner(it)
            } else {
                errorDialog(getString(R.string.error_unknown_body))
            }
        })
    }

    private fun showEmptySlider() {
        vp_banners.visibility = View.GONE
    }

    private fun showIsNotEmpty() {
        tv_loading.visibility = View.GONE
        rv_loans.visibility = View.VISIBLE
    }

    private fun showEmptyLoans() {
        rv_loans.visibility = View.GONE
        tv_blank_loans.visibility = View.VISIBLE
        tv_loading.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun setFinenessPrice(finenessPrice: FinenessPriceResponse) {
        for (i in 0 until finenessPrice.prices.size) {
            when (i) {
                FINENESS_FIRST -> {
                    tv_fineness_name1.text =
                        "Проба AU ${finenessPrice.prices[FINENESS_FIRST].title}"
                    tv_fineness_price1.text = "${finenessPrice.prices[FINENESS_FIRST].value} тг"
                }
                FINENESS_SECOND -> {
                    tv_fineness_name2.text =
                        "Проба AU ${finenessPrice.prices[FINENESS_SECOND].title}"
                    tv_fineness_price2.text = "${finenessPrice.prices[FINENESS_SECOND].value} тг"
                }
                FINENESS_THIRD -> {
                    tv_fineness_name3.text =
                        "Проба AU ${finenessPrice.prices[FINENESS_THIRD].title}"
                    tv_fineness_price3.text = "${finenessPrice.prices[FINENESS_THIRD].value} тг"
                }
            }
        }
    }

    private fun setSpinner(finenessPrice: FinenessPriceResponse) {
        val finenessList = mutableListOf<String>()

        for (i in 0 until finenessPrice.prices.size) {
            finenessList.add(finenessPrice.prices[i].title!!)
        }
        ArrayAdapter(
            requireContext(),
            R.layout.item_fineness_spinner,
            finenessList
        ).also { adapter ->
            spinner_fineness.adapter = adapter
        }

        spinner_fineness.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
        }

    }

    private fun initAutoScroll() {

    }

    private fun setProfile(profile: ProfileInfo) {
        tv_name.text = profile.fio
    }

    @SuppressLint("SetTextI18n")
    private fun setWeather(weatherData: WeatherData) {
        tv_temp.text = "${weatherData.temp} ºC"
        when (weatherData.icon) {
            CLEAR_SKY -> setImage(R.drawable.ic_icon_sunny)
            FEW_CLOUDS -> setImage(R.drawable.ic_few_clouds)
            SCATTERED_CLOUDS -> setImage(R.drawable.ic_scattered_clouds)
            BROKEN_CLOUDS -> setImage(R.drawable.ic_broken_clouds)
            SHOWER_RAIN -> setImage(R.drawable.ic_shower_rain)
            RAIN -> setImage(R.drawable.ic_rain)
            THUNDERSTORM -> setImage(R.drawable.ic_thunderstorm)
            SNOW -> setImage(R.drawable.ic_snow)
            MIST -> setImage(R.drawable.ic_mist)
        }
    }

    private fun setCurrency(currencyList: ArrayList<CurrencyList>) {
        for (i in 0 until currencyList.size) {
            when (i) {
                USA_CURRENCY -> {
                    tv_sale_usa.text = currencyList[USA_CURRENCY].sale.toString()
                    tv_purchase_usa.text = currencyList[USA_CURRENCY].purchase.toString()
                }
                EUROPA_CURRENCY -> {
                    tv_sale_europa.text = currencyList[EUROPA_CURRENCY].sale.toString()
                    tv_purchase_europa.text = currencyList[EUROPA_CURRENCY].purchase.toString()
                }
                RUSSIA_CURRENCY -> {
                    tv_sale_russia.text = currencyList[RUSSIA_CURRENCY].sale.toString()
                    tv_purchase_russia.text = currencyList[RUSSIA_CURRENCY].purchase.toString()
                }
            }
        }
    }

    private fun addLoans(loansList: ArrayList<Tickets>) {
        adapters.addLoans(loansList)
    }

    private fun addSliderList(sliderList: ArrayList<SlidersList>) {
        bannerAdapter.addImageSlider(sliderList)
    }

    @SuppressLint("SetTextI18n")
    fun onAlertDialog(loansList: Tickets) {
        var isPay = false

        alertDialog = Dialog(requireContext())
        alertDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog!!.setContentView(R.layout.alert_dialog_pay)

        val tvId: TextView = alertDialog!!.findViewById(R.id.tv_id)
        val tvLoans: TextView = alertDialog!!.findViewById(R.id.tv_loans)
        val tvAmount: TextView = alertDialog!!.findViewById(R.id.tv_renewal_amount)
        val tvDate: TextView = alertDialog!!.findViewById(R.id.tv_date)
        val tvTotalPrice: TextView = alertDialog!!.findViewById(R.id.tv_loan_amount)
        val btnPay: MaterialButton = alertDialog!!.findViewById(R.id.btn_pay_loan)

        var information = ""
        for (i in 0 until loansList.items.size) {
            information += "\n${loansList.items[i].Specification}\n"
        }

        tvLoans.text = information
        tvId.text = "№ " + loansList.ticketInfo.Number
        tvAmount.text = loansList.ticketInfo.TotalPayment + "тг"
        tvDate.text = formatDate(loansList.ticketInfo.WaitDate!!) + "г"
        tvTotalPrice.text = loansList.ticketInfo.totalDebt + "тг"

        btnPay.onClick {
            when (isPay) {
                true -> {
                    alertDialog!!.dismiss()
                    setNavigateToPin()
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

        alertDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.show()
    }

    private fun setNavigateToPin() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_homeFragment_to_validatePinFragment)
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun initTodayDate() {
        val format = SimpleDateFormat("dd.MM.yyyy")
        tv_this_day.text = "за " + format.format(Date()) + "г."
    }

    private fun setImage(drawable: Int) {
        iv_weather_icon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                drawable
            )
        )
    }

    private fun errorDialog(errorMsg: String) {
        if (!isDialogVisibility) {
            isDialogVisibility = true
            activity?.alert {
                title = getString(R.string.error_unknown_title)
                message = errorMsg
                isCancelable = false
                negativeButton(getString(R.string.dialog_ok)) {
                    setLoading(false)
                    it.dismiss()
                    isDialogVisibility = false
                }
            }?.show()
        }
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }
}