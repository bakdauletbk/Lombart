package kz.pillikan.lombart.content.view.about

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.*
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.view.AuthorizationActivity
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.about.AddressList
import kz.pillikan.lombart.content.viewmodel.about.AboutViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor
import java.util.*


class AboutFragment : BaseFragment(), MapObjectTapListener {

    private var mapKit: MapKit? = null
    private lateinit var viewModel: AboutViewModel
    private var isDialogVisibility = false

    companion object {
        const val LATITUDE = 42.330639
        const val LONGITUDE = 69.600967
        const val ZOOM = 12.0f
        const val AZIMUTH = 0.0f
        const val TILT = 0.0f
        const val DURATION = 0F
    }

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
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lets()
    }

    private fun lets() {
        initViewModel()
        initMap()
        updateFeed()
        initObservers()
    }

    private fun updateFeed() {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAddress()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialog()
        })
        viewModel.addressList.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setLoading(false)
                    setAddress(it)
                }
                false -> setLoading(false)
            }
        })
        viewModel.isUpdateApp.observe(viewLifecycleOwner, {
            when (it) {
                true -> showAlertDialog(
                    requireContext(),
                    getString(R.string.our_application_has_been_updated_please_update)
                )
            }
        })
        viewModel.isUnAuthorized.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                    viewModel.clearSharedPref()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.you_are_logged_in_under_your_account_on_another_device),
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(intentFor<AuthorizationActivity>())
                    activity?.finish()
                }
            }
        })

    }

    private fun errorDialog() {
        setLoading(false)
        errorDialog(getString(R.string.error_unknown_body))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setAddress(addressList: ArrayList<AddressList>) {
        for (i in Constants.ZERO until addressList.size) {

            val view = View(requireContext()).apply {
                background = requireContext().getDrawable(R.drawable.ic_point)
            }

            val pointCollection: MapObjectCollection = map_view.map.mapObjects.addCollection()

            val placeMark: PlacemarkMapObject = pointCollection.addPlacemark(
                Point(
                    addressList[i].latitude!!.toDouble(), addressList[i].longitude!!.toDouble()
                ),
                ViewProvider(view)
            )

            placeMark.userData = addressList[i]

            placeMark.addTapListener(this)
        }
    }

    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
        val userData: AddressList = mapObject.userData as AddressList
        initDetailInfoAddress(userData)
        return true
    }

    private fun initDetailInfoAddress(locationData: AddressList) {
        cl_detail_map.visibility = View.VISIBLE
        tv_address_map.text = locationData.address

        tv_date.text = locationData.work_time
        tv_content.text = locationData.content
        tv_title_map.text = locationData.name

        tv_close_detail.onClick {
            cl_detail_map.visibility = View.GONE
        }

        ll_call.onClick {
            call(locationData.phone.toString())
        }
        ll_location.onClick {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("geo:${locationData.latitude},${locationData.longitude}?z=11");
            startActivity(intent);
        }
        ll_whatsapp.onClick {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse(Constants.WHATSAPP_URI + locationData.whats_app)
            startActivity(intent)
        }
    }

    private fun initMap() {
        mapKit = MapKitFactory.getInstance()

        map_view.map.move(
            CameraPosition(Point(LATITUDE, LONGITUDE), ZOOM, AZIMUTH, TILT),
            com.yandex.mapkit.Animation(com.yandex.mapkit.Animation.Type.SMOOTH, DURATION),
            null
        )
    }

    override fun onStop() {
        map_view.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        map_view.onStart()
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun showAlertDialog(errorMsg: String) {
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


}

