package kz.pillikan.lombart.content.view.validate

import android.Manifest
import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
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
import kz.pillikan.lombart.content.viewmodel.validate.ValidatePinViewModel
import org.jetbrains.anko.sdk27.coroutines.onClick

class ValidatePinFragment : BaseFragment() {

    private lateinit var viewModel: ValidatePinViewModel
    private var validatePinRequest: ValidatePinRequest? = null

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
        if (isSdkVersionSupported() && isFingerprintAvailable() && isHardwareSupported() && isPermissionGranted()
        ) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                initBiometric()
            }
        }

        iv_finger_print.onClick {
            if (isSdkVersionSupported() && isFingerprintAvailable() && isHardwareSupported() && isPermissionGranted()
            ) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    initBiometric()
                }
            }
        }

        btn_create_pin.onClick {
            context?.let { it1 -> viewModel.checkNetwork(it1) }
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

        val callback = object : BiometricPrompt.AuthenticationCallback() {
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

        biometricPrompt.authenticate(
            getCancellationSignal(),
            activity?.mainExecutor!!, callback
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

    @SuppressLint("InlinedApi")
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
            errorAlertDialog()
        })
        viewModel.isNetworkConnection.observe(viewLifecycleOwner, {
            when (it) {
                true -> preparePin()
                false -> errorAlertDialog()
            }
        })
        viewModel.isSuccess.observe(viewLifecycleOwner, {
            when (it) {
                true -> findNavController().navigate(ValidatePinFragmentDirections.actionValidatePinFragmentToHomeFragment())
                false -> {
                    et_pin.text?.clear()
                    validatePinCodeAlert()
                }
            }
        })
    }

    private fun isSdkVersionSupported(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    private fun isHardwareSupported(): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(requireContext())
        return fingerprintManager.isHardwareDetected
    }

    private fun isFingerprintAvailable(): Boolean {
        val fingerprintManager = FingerprintManagerCompat.from(requireContext())
        return fingerprintManager.hasEnrolledFingerprints()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isPermissionGranted(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.USE_FINGERPRINT
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun validatePinCodeAlert() {
        setLoading(false)
        errorDialog(getString(R.string.error_pin_code_validate))
    }

    private fun errorAlertDialog() {
        setLoading(false)
        errorDialog(getString(R.string.error_unknown_body))
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

    private fun setLoading(loading: Boolean) {
        try {
            loadingView.visibility = if (loading) View.VISIBLE else View.GONE

        }catch (e:NullPointerException){

        }
    }

}