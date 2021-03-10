package kz.pillikan.lombart.authorization.view.register

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import kotlinx.android.synthetic.main.fragment_sms.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.request.CheckNumberRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.authorization.model.request.SignUpRequest
import kz.pillikan.lombart.authorization.viewmodel.register.SmsViewModel
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.helpers.convertSms
import kz.pillikan.lombart.common.views.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class SmsFragment : BaseFragment() {

    companion object {
        const val TAG = "SmsFragment"
        const val CREATE_USER = "createUser"
        const val SMS_CONSENT_REQUEST = 2
    }

    private lateinit var viewModel: SmsViewModel
    private var checkNumberRequest: CheckNumberRequest? = null
    private var bundle = Bundle()

    private val smsVerificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when(SmsRetriever.SMS_RETRIEVED_ACTION == intent.action){
                true ->{
                    val extras = intent.extras
                    val smsRetrieverStatus = extras!![SmsRetriever.EXTRA_STATUS] as Status?
                    when (smsRetrieverStatus!!.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val consentIntent =
                                extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                            try {
                                startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                            } catch (e: RuntimeException) {
                                Log.d(TAG, "onReceive: ${e.message}")
                            }
                        }
                        CommonStatusCodes.TIMEOUT -> {
                            Toast.makeText(context, getString(R.string.delay), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sms, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SMS_CONSENT_REQUEST -> {
                when (resultCode == Activity.RESULT_OK) {
                    true -> {
                        val message = data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                        et_sms.setText(convertSms(message!!))
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initViewModel()
        initSmsRetriever()
        iniListeners()
        initObservers()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SmsViewModel::class.java)
    }

    private fun initSmsRetriever() {
        setSendSms()
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        activity?.registerReceiver(smsVerificationReceiver, intentFilter)
    }

    private fun iniListeners() {
        btn_next.onClick {
            prepareLogin()
        }
        tv_retry_send_sms.onClick {
            setSendSms()
        }
        iv_back.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1).navigate(R.id.action_smsFragment_to_signInFragment)
            }
        }
    }

    private fun setSendSms() {
        val createUser = arguments?.getSerializable(
            CREATE_USER
        ) as SignUpRequest
        val phone = createUser.phone
        val phoneBase64 = base64encode(phone!!)
        val sendSmsRequest = SendSmsRequest(phone = phoneBase64)
        sendSms(sendSmsRequest)
    }

    private fun setFillSms() {
        activity?.let { SmsRetriever.getClient(it).startSmsUserConsent(null) }
    }

    private fun prepareLogin() {
        val signUpRequest = arguments?.getSerializable(
            CREATE_USER
        ) as SignUpRequest

        val phone = signUpRequest.phone
        val sms = et_sms.text.toString()

        //Base64encode
        val smsBase64 = base64encode(sms)
        val phoneBase64 = base64encode(phone!!)

        checkNumberRequest = CheckNumberRequest(phone = phoneBase64, activationcode = smsBase64)
        when (sms.isNotBlank()) {
            true -> verificationSms(checkNumberRequest!!)
            false -> Toast.makeText(
                this.context,
                getString(R.string.enter_your_sms_code),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun verificationSms(checkNumberRequest: CheckNumberRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.verificationSms(checkNumberRequest)
        }
    }

    private fun sendSms(sendSmsRequest: SendSmsRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.sendSms(sendSmsRequest = sendSmsRequest)
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorAlertDialog()
        })
        viewModel.isVerificationNumber.observe(viewLifecycleOwner, {
            when (it) {
                true -> initNavigation()
                false -> errorAlertDialog()
            }
        })
        viewModel.isSendSms.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                    setFillSms()
                    Toast.makeText(
                        this.context,
                        getString(R.string.send_sms),
                        Toast.LENGTH_LONG
                    ).show()
                }
                false -> errorAlertDialog()
            }
        })
    }

    private fun errorAlertDialog(){
        setLoading(false)
        errorDialog(getString(R.string.the_sms_you_entered_incorrectly))
    }

    private fun initNavigation() {
        val createUser = arguments?.getSerializable(
            CREATE_USER
        ) as SignUpRequest
        bundle = Bundle()
        bundle.putSerializable(CREATE_USER, createUser)
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_smsFragment_to_createPasswordFragment, bundle)
        }
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        btn_next.isCheckable = !loading
        et_sms.isEnabled = !loading
        tv_retry_send_sms.isEnabled = !loading
    }

}