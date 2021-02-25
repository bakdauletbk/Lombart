package kz.pillikan.lombart.authorization.view.register

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_create_password.*
import kotlinx.android.synthetic.main.fragment_create_password.et_password
import kotlinx.android.synthetic.main.fragment_create_password.loadingView
import kotlinx.android.synthetic.main.fragment_sigin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.request.SignUpRequest
import kz.pillikan.lombart.authorization.viewmodel.register.CreatePasswordViewModel
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.views.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert

class CreatePasswordFragment : BaseFragment() {

    companion object {
        const val TAG = "CreatePasswordFragment"
        const val CREATE_USER = "createUser"
    }

    private lateinit var viewModel: CreatePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_password, container, false)
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

        viewModel.isSuccess.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    view?.let { it1 ->
                        Navigation.findNavController(it1)
                            .navigate(R.id.action_createPasswordFragment_to_successfullyFragment)
                    }
                }
                false -> {
                    onSuccessFullDialog()
                }
            }
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(CreatePasswordViewModel::class.java)
    }

    private fun initListeners() {
        btn_proceed_.onClick { prepareLogin() }
        iv_back.onClick { activity?.onBackPressed() }
    }

    private fun prepareLogin() {
        val createUser = arguments?.getSerializable(
            CREATE_USER
        ) as SignUpRequest

        val password = et_password.text.toString()
        val password2 = et_password_again.text.toString()
        val ftoken = "sakfoas"
        val iin = createUser.iin
        val phone = createUser.phone

        //Base64 encode
        val passwordBase64 = Base64.encodeToString(password.toByteArray(), Base64.NO_WRAP)
        val password2Base64 = Base64.encodeToString(password2.toByteArray(), Base64.NO_WRAP)
        val ftokenBase64 = Base64.encodeToString(ftoken.toByteArray(), Base64.NO_WRAP)
        val phoneBase64 = Base64.encodeToString(phone?.toByteArray(), Base64.NO_WRAP)
        val iinBase64 = Base64.encodeToString(iin?.toByteArray(), Base64.NO_WRAP)

        val signUpRequest = SignUpRequest(
            iin = iinBase64,
            phone = phoneBase64,
            password = passwordBase64,
            password2 = password2Base64,
            ftoken = ftokenBase64
        )

        when (Validators.validatePassword(password) && Validators.validatePassword(password2) && password == password2) {
            true -> {
                createUser(signUpRequest)
            }
            false -> {
                Toast.makeText(context, "Ваши пароли не совпадают!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUser(signUpRequest: SignUpRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.createUser(signUpRequest)
        }
    }

    private fun onSuccessFullDialog() {
        alert {
            title = getString(R.string.error_auth_wrong_data_title)
            message = getString(R.string.error_auth_wrong_data_msg)
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
                btn_proceed_.isCheckable = false
                et_password.isEnabled = false
                et_password_again.isEnabled = false
            }
            false -> {
                loadingView.visibility = View.GONE
                btn_proceed_.isCheckable = true
                et_password.isEnabled = true
                et_password_again.isEnabled = true
            }
        }
    }


}