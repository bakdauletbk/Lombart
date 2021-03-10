package kz.pillikan.lombart.content.view.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.loadingView
import kotlinx.coroutines.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.convertDpToPixel
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.common.views.utils.FinenessPrice
import kz.pillikan.lombart.content.model.response.home.*
import kz.pillikan.lombart.content.viewmodel.home.HomeViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment() {

    private val adapters: LoansAdapter = LoansAdapter(this)
    private val currencyPrice: FinenessPrice = FinenessPrice(this)
    private lateinit var viewModel: HomeViewModel
    private val bannerAdapter by lazy { PagerAdapter(context) }
    private val intent = Intent()
    private var isDialogVisibility = false

    companion object {
        const val TAG = "HomeFragment"
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
        setUpdateFeed()
        initObservers()
        setBannerContent()
        setTodayDate()
    }

    private fun initViewPager() {
        vp_banners.adapter = bannerAdapter
        vp_banners.apply {
            setPadding(
                convertDpToPixel(Constants.PADDING),
                Constants.PADDING_TOP,
                convertDpToPixel(Constants.PADDING),
                Constants.PADDING_BOTTOM
            )
            pageMargin = convertDpToPixel(Constants.PAGE_MARGIN)
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

    private fun setUpdateFeed() {
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
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getHeadText()
        }
    }

    private fun initListeners() {
        ll_technical_support.onClick {
            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.appealFragment)
            }
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorAlertDialog(getString(R.string.error_unknown_body))
        })
        viewModel.loanList.observe(viewLifecycleOwner, {
            when (it) {
                null -> {
                    showEmptyLoans()
                    errorAlertDialog(getString(R.string.error_unknown_body))
                }
                else -> {
                    setLoading(false)
                    showIsNotEmpty()
                    addLoans(it)
                }
            }
        })
        viewModel.currencyList.observe(viewLifecycleOwner, {
            when (it) {
                null -> errorAlertDialog(getString(R.string.error_unknown_body))
                else -> {
                    setLoading(false)
                    currencyPrice.setCurrency(it)
                }
            }
        })
        viewModel.weatherData.observe(viewLifecycleOwner, {
            when (it) {
                null -> errorAlertDialog(getString(R.string.error_unknown_body))
                else -> {
                    setLoading(false)
                    setWeather(it)
                }
            }
        })
        viewModel.profileInfo.observe(viewLifecycleOwner, {
            when (it) {
                null -> errorAlertDialog(getString(R.string.error_unknown_body))
                else -> {
                    setLoading(false)
                    setProfile(it)
                }
            }
        })
        viewModel.slidersList.observe(viewLifecycleOwner, {
            when (it) {
                null -> {
                    showEmptySlider()
                    errorAlertDialog(getString(R.string.error_unknown_body))
                }
                else -> {
                    setLoading(false)
                    addSliderList(it)
                }
            }
        })
        viewModel.finenessPrice.observe(viewLifecycleOwner, {
            when (it) {
                null -> errorAlertDialog(getString(R.string.error_unknown_body))
                else -> {
                    setLoading(false)
                    currencyPrice.setFinenessPrice(it)
                    currencyPrice.setSpinner(it)
                    currencyPrice.setLoanAmount(it)
                }
            }
        })
        viewModel.headText.observe(viewLifecycleOwner, {
            when (it) {
                null -> errorAlertDialog(getString(R.string.error_unknown_body))
                else -> {
                    setLoading(false)
                    setHeadText(it)
                }
            }
        })
    }

    private fun setHeadText(titleResponse: TitleResponse) {
        tv_title_home.text = titleResponse.title
        tv_description_home.text = titleResponse.text1
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

    private fun setBannerContent() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                if (vp_banners.currentItem != bannerAdapter.count.minus(Constants.ONE)) {
                    vp_banners.setCurrentItem(vp_banners.currentItem.plus(Constants.ONE), true)
                } else {
                    vp_banners.setCurrentItem(Constants.ZERO, true)
                }
                delay(Constants.TIME_MILLIS)
                setBannerContent()
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
            }
        }
    }

    private fun setProfile(profile: ProfileInfo) {
        tv_name.text = profile.fio
    }

    @SuppressLint("SetTextI18n")
    private fun setWeather(weatherData: WeatherData) {
        tv_temp.text = weatherData.temp + Constants.CELSIUS
        when (weatherData.icon) {
            Constants.CLEAR_SKY -> setImage(R.drawable.ic_icon_sunny)
            Constants.FEW_CLOUDS -> setImage(R.drawable.ic_few_clouds)
            Constants.SCATTERED_CLOUDS -> setImage(R.drawable.ic_scattered_clouds)
            Constants.BROKEN_CLOUDS -> setImage(R.drawable.ic_broken_clouds)
            Constants.SHOWER_RAIN -> setImage(R.drawable.ic_shower_rain)
            Constants.RAIN -> setImage(R.drawable.ic_rain)
            Constants.THUNDERSTORM -> setImage(R.drawable.ic_thunderstorm)
            Constants.SNOW -> setImage(R.drawable.ic_snow)
            Constants.MIST -> setImage(R.drawable.ic_mist)
        }
    }

    private fun addLoans(loansList: ArrayList<Tickets>) {
        adapters.addLoans(loansList)
    }

    private fun addSliderList(sliderList: ArrayList<SlidersList>) {
        bannerAdapter.addImageSlider(sliderList)
    }

    @SuppressLint("SetTextI18n")
    fun onPayAlertDialog(loansList: Tickets) {
        showCustomAlert(Constants.ALERT_TYPE_LOAN_DETAILS, loansList)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setTodayDate() {
        val format = SimpleDateFormat(Constants.FORMAT_DATE)
        tv_this_day.text = Constants.FOR + format.format(Date()) + Constants.YEARS
    }

    private fun setImage(drawable: Int) {
        iv_weather_icon.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                drawable
            )
        )
    }

    private fun errorAlertDialog(errorMsg: String) {
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