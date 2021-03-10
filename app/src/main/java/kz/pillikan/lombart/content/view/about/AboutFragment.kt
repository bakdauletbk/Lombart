package kz.pillikan.lombart.content.view.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import kotlinx.android.synthetic.main.fragment_about.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.about.AboutResponse
import kz.pillikan.lombart.content.model.response.about.AddressList
import kz.pillikan.lombart.content.viewmodel.about.AboutViewModel
import java.util.ArrayList

class AboutFragment : BaseFragment() {

    private var mapKit: MapKit? = null
    private lateinit var viewModel: AboutViewModel

    companion object {
        const val LATITUDE = 42.330639
        const val LONGITUDE = 69.600967
        const val ZOOM = 11.0f
        const val AZIMUTH = 0.0f
        const val TILT = 0.0f
        const val DURATION = 0F
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAbout()
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
            if (it != null) {
                setLoading(false)
                setAddress(it)
            } else errorDialog()
        })
        viewModel.about.observe(viewLifecycleOwner,{
            when(it){
                null -> errorDialog()
                else -> {
                    setLoading(false)
                    setAboutText(it)
                }
            }
        })
    }

    private fun setAboutText(aboutResponse: AboutResponse) {
        tv_text1.text = aboutResponse.text2
        tv_text2.text = aboutResponse.text3
    }

    private fun errorDialog() {
        setLoading(false)
        errorDialog(getString(R.string.error_unknown_body))
    }

    private fun setAddress(addressList: ArrayList<AddressList>) {
        for (i in 0 until addressList.size) {
            map_view.map.mapObjects.addPlacemark(
                Point(
                    addressList[i].latitude!!.toDouble(), addressList[i].longitude!!.toDouble()
                )
            )
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

}