package kz.pillikan.lombart.content.view.home

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_validate_pin.*
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

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback =
        @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                findNavController().navigate(ValidatePinFragmentDirections.actionValidatePinFragmentToHomeFragment())
            }
        }


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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            initBiometric()
        }
        btn_create_pin.onClick {
            preparePin()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun initBiometric() {

        initPermission()

        val biometricPrompt = BiometricPrompt.Builder(requireContext())
            .setTitle(getString(R.string.fingerprint_login))
            .setDescription(getString(R.string.touch_the_fingerprint_reader))
            .setNegativeButton(
                getString(R.string.cancel),
                activity?.mainExecutor!!,
                DialogInterface.OnClickListener { _, _ ->
                    Toast.makeText(
                        context,
                        getString(R.string.authentication_canceled),
                        Toast.LENGTH_LONG
                    ).show()
                }).build()

        biometricPrompt.authenticate(
            getCancellationSignal(),
            activity?.mainExecutor!!, authenticationCallback
        )
    }

    private fun getCancellationSignal(): CancellationSignal {
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            Toast.makeText(
                context,
                getString(R.string.auth_has_been_canceled),
                Toast.LENGTH_LONG
            ).show()
        }

        return cancellationSignal
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initPermission(): Boolean {
        val keyguardManager: KeyguardManager =
            activity?.getSystemService(Context.KEYGUARD_SERVICE)!! as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            Toast.makeText(
                context,
                getString(R.string.fingerprint_authentication_is_not_enabled_settings),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                context,
                getString(R.string.permission_is_not_included),
                Toast.LENGTH_LONG
            ).show()
            return false
        }
        return if (activity?.packageManager!!.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
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
            false -> Toast.makeText(
                context,
                getString(R.string.enter_your_pin_code),
                Toast.LENGTH_LONG
            ).show()
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