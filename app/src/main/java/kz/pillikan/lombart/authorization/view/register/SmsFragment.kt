package kz.pillikan.lombart.authorization.view.register

import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_sms.*
import kotlinx.android.synthetic.main.fragment_sms.iv_back
import kotlinx.android.synthetic.main.fragment_sms.loadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.request.CheckNumberRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.authorization.model.request.SignUpRequest
import kz.pillikan.lombart.authorization.viewmodel.register.SmsViewModel
import kz.pillikan.lombart.common.views.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class SmsFragment : BaseFragment() {

    private lateinit var viewModel: SmsViewModel

    companion object {
        const val CREATE_USER = "createUser"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initViewModel()
        iniListeners()
        initObservers()
    }

    private fun iniListeners() {
        btn_next.onClick {
            prepareLogin()
        }
        tv_retry_send_sms.onClick {
            prepareNumber()
        }
        iv_back.onClick { activity?.onBackPressed() }
    }

    private fun prepareNumber() {
        val createUser = arguments?.getSerializable(
            CREATE_USER
        ) as SignUpRequest
        val phone = createUser.phone

        val phoneBase64 =  Base64.encodeToString(phone?.toByteArray(), Base64.NO_WRAP)

        val sendSmsRequest = SendSmsRequest(phone = phoneBase64)
        sendSms(sendSmsRequest)
    }

    private fun prepareLogin() {

        val signUpRequest = arguments?.getSerializable(
            CREATE_USER
        ) as SignUpRequest

        val phone = signUpRequest.phone
        val sms = et_sms.text.toString()

        //Base64encode
        val smsBase64 = Base64.encodeToString(sms.toByteArray(), Base64.NO_WRAP)

        val checkNumberRequest = CheckNumberRequest(phone = phone, activationcode = smsBase64)
        when (sms.isNotBlank()) {
            true -> verificationSms(checkNumberRequest)
            false -> {
                Toast.makeText(
                    this.context,
                    "Введите смс код!",
                    Toast.LENGTH_LONG
                ).show()
            }
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
            errorDialog(getString(R.string.error_unknown_body))
        })

        viewModel.isVerificationNumber.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    val createUser = arguments?.getSerializable(
                        CREATE_USER
                    ) as SignUpRequest

                    val bundle = Bundle()
                    bundle.putSerializable(CREATE_USER, createUser)
                    view?.let { it1 ->
                        Navigation.findNavController(it1)
                            .navigate(R.id.action_smsFragment_to_createPasswordFragment, bundle)
                    }
                }
                false -> {
                    errorDialog("Введенные вами смс некорректно")
                }
            }
        })

        viewModel.isSendSms.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                }
                false -> {
                    errorDialog("Введенные вами смс некорректно")
                }
            }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SmsViewModel::class.java)
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
        when (loading) {
            true -> {
                loadingView.visibility = View.VISIBLE
                btn_next.isCheckable = false
                et_sms.isEnabled = false
                tv_retry_send_sms.isEnabled = false
            }
            false -> {
                loadingView.visibility = View.GONE
                btn_next.isCheckable = true
                et_sms.isEnabled = true
                tv_retry_send_sms.isEnabled = true
            }
        }
    }


}