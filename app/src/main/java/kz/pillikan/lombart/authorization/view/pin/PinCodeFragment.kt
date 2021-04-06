package kz.pillikan.lombart.authorization.view.pin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_pin_code.*
import kotlinx.android.synthetic.main.fragment_pin_code.loadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.request.PinCodeRequest
import kz.pillikan.lombart.authorization.viewmodel.pin.PinCodeViewModel
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.view.FoundationActivity
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor

class PinCodeFragment : BaseFragment() {

    private lateinit var viewModel: PinCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pin_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initViewModel()
        initListeners()
        initObservers()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(PinCodeViewModel::class.java)
    }

    private fun initListeners() {
        btn_create_pin.onClick { preparePinCode() }
    }

    private fun preparePinCode() {
        val pinCode = et_access_pin.text.toString()
        val pinCode2 = et_access_pin_repeat.text.toString()

        //Base64 encode
        val pinCodeBase64 = base64encode(pinCode)
        val pinCode2Base64 = base64encode(pinCode2)

        val pinCodeRequest = PinCodeRequest(pin1 = pinCodeBase64, pin2 = pinCode2Base64)

        when (Validators.validatePinCode(pinCode) && Validators.validatePinCode(pinCode2) && pinCode == pinCode2) {
            true -> savePinCode(pinCodeRequest)
            false -> Toast.makeText(context, getString(R.string.enter_your_pin_code), Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePinCode(pinCodeRequest: PinCodeRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.savePinCode(pinCodeRequest)
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            setLoading(false)
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.isSuccess.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    startActivity(intentFor<FoundationActivity>())
                    requireActivity().finish()

                }
                false -> {
                    et_access_pin.text?.clear()
                    et_access_pin_repeat.text?.clear()
                    Toast.makeText(context, getString(R.string.save_error), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        btn_create_pin.isCheckable = !loading
        et_access_pin.isEnabled = !loading
        et_access_pin_repeat.isEnabled = !loading
    }

}