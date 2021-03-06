package kz.pillikan.lombart.authorization.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_create_password.*
import kotlinx.android.synthetic.main.fragment_create_password.et_password
import kotlinx.android.synthetic.main.fragment_create_password.loadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.request.SignUpRequest
import kz.pillikan.lombart.authorization.view.AuthorizationActivity
import kz.pillikan.lombart.authorization.viewmodel.register.CreatePasswordViewModel
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.views.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.intentFor

class CreatePasswordFragment : BaseFragment() {

    companion object {
        const val TAG = "CreatePasswordFragment"
        const val CREATE_USER = "createUser"
    }

    private lateinit var viewModel: CreatePasswordViewModel
    private var signUpRequest: SignUpRequest? = null

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

    private fun createFirebaseToken() {
        viewModel.createFMToken()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(CreatePasswordViewModel::class.java)
    }

    private fun initListeners() {
        btn_proceed_.onClick {
            createFirebaseToken()
        }
        iv_back.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_createPasswordFragment_to_signInFragment3)
            }
        }
    }

    private fun prepareLogin(fToken: String) {
        val createUser = arguments?.getSerializable(
            CREATE_USER
        ) as SignUpRequest

        val password = et_password.text.toString()
        val password2 = et_password_again.text.toString()
        val iin = createUser.iin
        val phone = createUser.phone

        //Base64 encode
        val passwordBase64 = base64encode(password)
        val password2Base64 = base64encode(password2)
        val fTokenBase64 = base64encode(fToken)
        val iinBase64 = base64encode(iin!!)
        val phoneBase64 = base64encode(phone!!)

        signUpRequest = SignUpRequest(
            iin = iinBase64,
            phone = phoneBase64,
            password = passwordBase64,
            password2 = password2Base64,
            ftoken = fTokenBase64
        )

        when (Validators.validatePassword(password) && Validators.validatePassword(password2) && password == password2) {
            true -> createUser(signUpRequest!!)
            false -> Toast.makeText(
                context,
                getString(R.string.your_passwords_do_not_match),
                Toast.LENGTH_SHORT
            ).show()
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
                    initNavigation()
                }
                false -> {
                    onSuccessFullDialog()
                }
            }
        })
        viewModel.firebaseToken.observe(viewLifecycleOwner, {
            prepareLogin(it)
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

    private fun initNavigation() {
        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_createPasswordFragment_to_successfullyFragment)
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

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        btn_proceed_.isCheckable = !loading
        et_password.isEnabled = !loading
        et_password_again.isEnabled = !loading
    }

}