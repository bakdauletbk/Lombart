package kz.pillikan.lombart.content.view.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.loadingView
import kotlinx.coroutines.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.home.*
import kz.pillikan.lombart.content.viewmodel.home.HomeViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.lang.Runnable

class HomeFragment : BaseFragment() {

    private val adapters: LoansAdapter = LoansAdapter(this)
    private lateinit var viewModel: HomeViewModel
    private var alertDialog: Dialog? = null
    private val bannerAdapter by lazy { PagerAdapter(context) }
    val handler = Handler()

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
        const val FIRST_CURRENCY = 0
        const val SECOND_CURRENCY = 1
        const val THIRD_CURRENCY = 2
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
    }

    private fun initViewPager() {
        vp_banners.adapter = bannerAdapter
        vp_banners.apply {
            setPadding(convertDpToPixel(20f), 0, convertDpToPixel(20f), 0)
            pageMargin = convertDpToPixel(12f)
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
        viewModel.loanList.observe(viewLifecycleOwner, {
            if (it != null) {
                addLoans(it)
                setLoading(false)
            } else {
                rv_loans.visibility = View.GONE
                tv_blank_loans.visibility = View.VISIBLE
                errorDialog(getString(R.string.error_unknown_body))
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
            }
        })
    }

    private fun addSliderList(sliderList: java.util.ArrayList<SlidersList>) {
        bannerAdapter.addImageSlider(sliderList)
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
                FIRST_CURRENCY -> {
                    tv_sale_usa.text = currencyList[FIRST_CURRENCY].sale.toString()
                    tv_purchase_usa.text = currencyList[FIRST_CURRENCY].purchase.toString()
                }
                SECOND_CURRENCY -> {
                    tv_sale_europa.text = currencyList[SECOND_CURRENCY].sale.toString()
                    tv_purchase_europa.text = currencyList[SECOND_CURRENCY].purchase.toString()
                }
                THIRD_CURRENCY -> {
                    tv_sale_russia.text = currencyList[THIRD_CURRENCY].sale.toString()
                    tv_purchase_russia.text = currencyList[THIRD_CURRENCY].purchase.toString()
                }
            }
        }
    }

    private fun addLoans(loansList: ArrayList<Tickets>) {
        adapters.addLoans(loansList)
    }

    @SuppressLint("SetTextI18n")
    fun onAlertDialog(loansList: Tickets) {
        alertDialog = Dialog(requireContext())
        alertDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog!!.setContentView(R.layout.alert_dialog_pay)

        val tvId: TextView = alertDialog!!.findViewById(R.id.tv_id)
        val tvLoans: TextView = alertDialog!!.findViewById(R.id.tv_loans)
        val tvAmount: TextView = alertDialog!!.findViewById(R.id.tv_renewal_amount)
        val tvDate: TextView = alertDialog!!.findViewById(R.id.tv_date)
        val tvTotalPrice: TextView = alertDialog!!.findViewById(R.id.tv_loan_amount)

        var information = ""
        for (i in 0 until loansList.items.size) {
            information += "\n${loansList.items[i].Specification}\n"
        }

        tvLoans.text = information
        tvId.text = "№ ${loansList.ticketInfo.Number}"
        tvAmount.text = "${loansList.ticketInfo.TotalPayment}тг"
        tvDate.text = "${loansList.ticketInfo.WaitDate}г"
        tvTotalPrice.text = "${loansList.ticketInfo.totalDebt}тг"
        alertDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.show()
    }

    private fun setImage(drawable: Int) {
        iv_weather_icon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                drawable
            )
        )
    }

    private fun convertDpToPixel(dp: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private fun errorDialog(errorMsg: String) {
        activity?.alert {
            title = getString(R.string.error_unknown_title)
            message = errorMsg
            isCancelable = false
            negativeButton(getString(R.string.dialog_ok)) {
                setLoading(false)
                it.dismiss()
            }
        }?.show()
    }

    private fun setLoading(loading: Boolean) {
        when (loading) {
            true -> {
                loadingView.visibility = View.VISIBLE
                nsvDelivery.isScrollContainer = false
            }
            false -> {
                loadingView.visibility = View.GONE
                nsvDelivery.isScrollContainer = true
            }
        }
    }
}