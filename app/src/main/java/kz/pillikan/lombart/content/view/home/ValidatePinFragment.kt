package kz.pillikan.lombart.content.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_validate_pin.*
import kotlinx.android.synthetic.main.fragment_validate_pin.loadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.request.home.ValidatePinRequest
import kz.pillikan.lombart.content.viewmodel.home.ValidatePinViewModel
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class ValidatePinFragment : BaseFragment() {

    private lateinit var viewModel: ValidatePinViewModel
    private var validatePinRequest: ValidatePinRequest? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_validate_pin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lets()
    }

    private fun lets() {
        initViewModel()
        setupListeners()
        initObservers()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ValidatePinViewModel::class.java)
    }

    private fun setupListeners() {
        btn_create_pin.onClick {
            preparePin()
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.isSuccess.observe(viewLifecycleOwner, {
            when (it) {
                true -> findNavController().navigate(ValidatePinFragmentDirections.actionValidatePinFragmentToHomeFragment())
                false -> errorDialog(getString(R.string.error_unknown_body))
            }
        })
    }

    private fun preparePin() {
        val pin = et_pin.text.toString()
        val pinBase64 = base64encode(pin)
        validatePinRequest = ValidatePinRequest(pin = pinBase64)

        when (Validators.validatePinCode(pin)) {
            true -> setValidatePin(validatePinRequest!!)
            false -> Toast.makeText(context, "Введите пин код!", Toast.LENGTH_LONG).show()
        }
    }

    private fun setValidatePin(validatePinRequest: ValidatePinRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.validatePin(validatePinRequest)
        }
    }

    private fun errorDialog(errorMsg: String) {
        alert {
            title = getString(R.string.error_unknown_title)
            message = errorMsg
            isCancelable = false
            positiveButton(getString(R.string.dialog_retry)) { dialog ->
                setLoading(false)
                dialog.dismiss()
            }
            negativeButton(getString(R.string.dialog_exit)) {
                activity?.finish()
            }
        }.show()
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }


}