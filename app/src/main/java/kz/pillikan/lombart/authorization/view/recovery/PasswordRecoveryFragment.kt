package kz.pillikan.lombart.authorization.view.recovery

import android.graphics.Color
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_password_recovery.*
import kotlinx.android.synthetic.main.fragment_password_recovery.et_iin
import kotlinx.android.synthetic.main.fragment_password_recovery.loadingView
import kotlinx.android.synthetic.main.fragment_sms.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.request.CheckNumberRequest
import kz.pillikan.lombart.authorization.model.request.CheckUserRequest
import kz.pillikan.lombart.authorization.model.request.ResetPasswordRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.authorization.model.response.CheckResponse
import kz.pillikan.lombart.authorization.viewmodel.recovery.PasswordRecoveryViewModel
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.views.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class PasswordRecoveryFragment : BaseFragment() {

    private lateinit var viewModel: PasswordRecoveryViewModel

    companion object {
        const val SEND_SMS = "SEND_SMS"
        const val RESET_PASSWORD = "RESET_PASSWORD"
        const val CONFIRM = "Подтвердить"
        const val REESTABLISH = "Восстановить"
        const val VERIFICATION_NUMBER = "VERIFICATION_NUMBER"
    }

    private var sendSmsRequest: SendSmsRequest? = null
    private var checkUserRequest: CheckUserRequest? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar_recovery.setNavigationIcon(R.drawable.ic_arrow_black)
        toolbar_recovery.setTitleTextColor(Color.BLACK)
        toolbar_recovery.setNavigationOnClickListener {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_passwordRecoveryFragment_to_signInFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_recovery, container, false)
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

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.isCheckUser.observe(viewLifecycleOwner, {
            if (it != null) {
                setLoading(false)
                setResetPassword(SEND_SMS)
                initSpinner(it)
                btn_reestablish.text = CONFIRM
            } else {
                Toast.makeText(
                    this.context,
                    getString(R.string.you_entered_is_incorrect),
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        viewModel.isSendSms.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                    setResetPassword(VERIFICATION_NUMBER)
                    btn_reestablish.onClick {
                        prepareValidatePhone()
                    }
                }
                false -> {
                    errorDialog(getString(R.string.error_unknown_body))
                }
            }
        })
        viewModel.isVerificationNumber.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                    setResetPassword(RESET_PASSWORD)
                    btn_reestablish.text = REESTABLISH
                    btn_reestablish.onClick {
                        preparePassword()
                    }
                }
                false -> {
                    errorDialog(getString(R.string.error_unknown_body))
                }
            }
        })
        viewModel.isResetPassword.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    Toast.makeText(
                        context,
                        getString(R.string.password_changed_successfully),
                        Toast.LENGTH_LONG
                    ).show()
                    view?.let { it1 ->
                        Navigation.findNavController(it1).navigate(R.id.signInFragment)
                    }
                }
                false -> {
                    errorDialog(getString(R.string.error_unknown_body))
                }
            }
        })
    }

    private fun preparePassword() {
        val password = et_password.text.toString()
        val password2 = et_password2.text.toString()

        //Base64 encode
        val passwordBase64 = base64encode(password)
        val password2Base64 = base64encode(password2)

        val resetPasswordRequest = ResetPasswordRequest(
            iin = checkUserRequest!!.iin,
            phone = sendSmsRequest!!.phone,
            password = passwordBase64,
            password2 = password2Base64
        )

        when (Validators.validatePassword(password) && Validators.validatePassword(password2) && password == password2) {
            true -> {
                resetPassword(resetPasswordRequest)
            }
            false -> {
                Toast.makeText(context, "Ваши пароли не совпадают!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.resetPassword(resetPasswordRequest)
        }
    }

    private fun prepareValidatePhone() {
        val temporary = et_temporary_password.text.toString()
        val phone = sendSmsRequest!!.phone
        //Base64 encode
        val temporaryBase64 = base64encode(temporary)

        val checkNumberRequest =
            CheckNumberRequest(phone = phone, activationcode = temporaryBase64)

        when (temporary.isNotEmpty()) {
            true -> verificationNumber(checkNumberRequest)
            false -> Toast.makeText(
                requireContext(),
                "Введенные вами смс некорректны!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun verificationNumber(checkNumberRequest: CheckNumberRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.verificationNumber(checkNumberRequest)
        }
    }

    private fun initSpinner(checkResponse: CheckResponse) {
        val phoneList = mutableListOf<String>()

        for (i in 0 until checkResponse.data.size) {
            phoneList.add(checkResponse.data[i].phone.toString())
        }

        ArrayAdapter(requireContext(), R.layout.item_phone_spinner, phoneList).also { adapter ->
            spinner_phone.adapter = adapter
        }

        spinner_phone.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                prepareNumber(checkResponse.data[position].phone.toString())
            }
        }
    }

    private fun prepareNumber(phone: String) {
        val phoneBase64 = base64encode(phone)

        sendSmsRequest = SendSmsRequest(phone = phoneBase64)

        btn_reestablish.onClick {
            sendSms(sendSmsRequest!!)
        }
    }

    private fun sendSms(sendSmsRequest: SendSmsRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.sendSms(sendSmsRequest)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(PasswordRecoveryViewModel::class.java)
    }

    private fun initListeners() {
        btn_reestablish.onClick {
            prepareIin()
        }
    }

    private fun prepareIin() {
        val iin = et_iin.text.toString()

        //Base64 encode
        val iinBase64 = base64encode(iin)

        checkUserRequest = CheckUserRequest(iin = iinBase64)

        when (Validators.validateInn(iin)) {
            true -> checkUsers(checkUserRequest!!)
            false -> Toast.makeText(
                this.context,
                "Введенные вами данные некорректны!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkUsers(checkUserRequest: CheckUserRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.checkUser(checkUserRequest)
        }
    }

    private fun setResetPassword(reset: String) {
        when (reset) {
            SEND_SMS -> {
                et_iin.isEnabled = false
                ll_phone.visibility = View.VISIBLE
            }
            VERIFICATION_NUMBER -> {
                et_iin.isEnabled = false
                spinner_phone.isEnabled = false
                ll_verification_number.visibility = View.VISIBLE
            }
            RESET_PASSWORD -> {
                et_iin.isEnabled = false
                spinner_phone.isEnabled = false
                et_temporary_password.isEnabled = false
                ll_password.visibility = View.VISIBLE
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun errorDialog(errorMsg: String) {
        alert {
            title = getString(R.string.error_unknown_title)
            message = errorMsg
            isCancelable = false
            negativeButton(getString(R.string.dialog_ok)) {
                setLoading(false)
                it.dismiss()
            }
        }.show()
    }

}