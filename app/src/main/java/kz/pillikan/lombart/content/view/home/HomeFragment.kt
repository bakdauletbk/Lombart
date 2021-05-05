package kz.pillikan.lombart.content.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.view.AuthorizationActivity
import kz.pillikan.lombart.common.helpers.convertDpToPixel
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.common.views.utils.FinenessPriceCalculate
import kz.pillikan.lombart.content.model.response.home.*
import kz.pillikan.lombart.content.view.FoundationActivity
import kz.pillikan.lombart.content.viewmodel.home.HomeViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : BaseFragment() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private var isOtherAuthShowOnce = false
    private var isUpdateAppShowOnce = false
    private var isErrorShowOnce = false

    private val adapters: LoansAdapter = LoansAdapter(this)
    private val currencyPrice: FinenessPriceCalculate = FinenessPriceCalculate(this)
    private lateinit var viewModel: HomeViewModel
    private val bannersAdapter by lazy { PagerAdapter(context) }
    private var isDialogVisibility = false
    private var foundationActivity: FoundationActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
        }
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
        initActivity()
        setLanguage()
        initViewPager()
        initRecyclerView()
        initListeners()
        setUpdateFeed()
        initObservers()
        setBannerContent()
        setTodayDate()
    }

    private fun initActivity() {
        foundationActivity = activity as FoundationActivity?
    }

    private fun setLanguage() {
        ll_kz.onClick {
            changeLanguage(Constants.KAZ)
        }
        ll_rus.onClick {
            changeLanguage(Constants.RUS)
        }
    }

    private fun changeLanguage(language: String) {
        foundationActivity?.setLocaleLanguage(language)
        viewModel.setLanguage(language)
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.homeFragment)
        }
        foundationActivity?.setTitleMenu()
    }

    private fun initViewPager() {
        vp_banners.adapter = bannersAdapter
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
        viewModel.getLanguage()
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
            foundationActivity?.navigateToAppeal()
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            if (!isErrorShowOnce) {
                errorAlertDialog(getString(R.string.error_unknown_body))
                isErrorShowOnce = true
            }
        })
        viewModel.loanList.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setLoading(false)
                    showIsNotEmpty()
                    addLoans(it)
                }
                false -> showEmptyLoans()
            }
        })
        viewModel.currencyList.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setLoading(false)
                    currencyPrice.setCurrency(it)
                }
                false -> errorAlertDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.weatherData.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setLoading(false)
                    setWeather(it)
                }
                false -> errorAlertDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.profileInfo.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setLoading(false)
                    setProfile(it)
                }
                false -> errorAlertDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.slidersList.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setLoading(false)
                    showSliders()
                    addSliderList(it)
                }
                false -> {
                    hideEmptySlider()
                    errorAlertDialog(getString(R.string.error_unknown_body))
                }
            }
        })
        viewModel.finenessPrice.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setLoading(false)
                    currencyPrice.setFinenessPrice(it)
                    currencyPrice.setSpinner(it)
                    currencyPrice.setLoanAmount(it)
                }
                false -> errorAlertDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.headText.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setLoading(false)
                    setHeadText(it)
                }
                false -> errorAlertDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.getLanguage.observe(viewLifecycleOwner, {
            when (it) {
                Constants.KAZ -> showLanguage(ll_kz, tv_kaz, Constants.KAZ)
                Constants.RUS -> showLanguage(ll_rus, tv_rus, Constants.RUS)
            }
        })
        viewModel.isUpdateApp.observe(viewLifecycleOwner, {
            when (it) {
                true ->
                    if (!isUpdateAppShowOnce) {
                        showAlertDialog(
                            requireContext(),
                            getString(R.string.our_application_has_been_updated_please_update)
                        )
                        isUpdateAppShowOnce = true
                    }
            }
        })
        viewModel.isUnAuthorized.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    if (!isOtherAuthShowOnce) {
                        setLoading(false)
                        viewModel.clearSharedPref()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.you_are_logged_in_under_your_account_on_another_device),
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(intentFor<AuthorizationActivity>())
                        isOtherAuthShowOnce = true
                        activity?.finish()
                    }
                }
            }
        })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showLanguage(linearLayout: LinearLayout, textView: TextView, lang: String) {
        linearLayout.background = resources.getDrawable(R.drawable.shape_language)
        textView.setTextColor(resources.getColor(R.color.white))
        foundationActivity?.setLocaleLanguage(lang)
        foundationActivity?.setTitleMenu()
    }

    private fun showSliders() {
        vp_banners.visibility = View.VISIBLE
    }

    private fun setHeadText(titleResponse: TitleResponse) {
        tv_title_home.text = titleResponse.title
        tv_description_home.text = titleResponse.text1
    }

    private fun hideEmptySlider() {
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
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (vp_banners.currentItem != bannersAdapter.count.minus(Constants.ONE)) {
                    vp_banners.setCurrentItem(
                        vp_banners.currentItem.plus(Constants.ONE),
                        true
                    )
                } else {
                    vp_banners.setCurrentItem(Constants.ZERO, true)
                }
                delay(Constants.TIME_MILLIS)

                setBannerContent()
            } catch (e: Exception) {
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
        bannersAdapter.addImageSlider(sliderList)
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