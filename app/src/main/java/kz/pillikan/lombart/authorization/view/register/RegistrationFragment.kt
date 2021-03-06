package kz.pillikan.lombart.authorization.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_registration.loadingView
import kotlinx.android.synthetic.main.fragment_registration.spinner_phone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.request.CheckUserRequest
import kz.pillikan.lombart.authorization.model.request.SignUpRequest
import kz.pillikan.lombart.authorization.model.response.CheckResponse
import kz.pillikan.lombart.authorization.view.AuthorizationActivity
import kz.pillikan.lombart.authorization.viewmodel.register.RegistrationViewModel
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.views.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor

class RegistrationFragment : BaseFragment() {

    private lateinit var viewModel: RegistrationViewModel

    companion object {
        const val TAG = "RegistrationFragment"
        const val CREATE_USER = "createUser"
    }

    private var signUpRequest: SignUpRequest? = null
    private var checkUserRequest: CheckUserRequest? = null
    private val bundle = Bundle()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
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
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
    }

    private fun initListeners() {
        btn_create.onClick { prepareCheckIin() }
        iv_back.onClick {
            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.action_registrationFragment_to_signInFragment)
            }
        }
    }

    private fun prepareCheckIin() {
        val iin = et_enter_iin.text.toString()

        //Base64encode
        val iinBase64 = base64encode(iin)

        //check users iin
        checkUserRequest = CheckUserRequest(iin = iinBase64)

        //send sms phone
        when (Validators.validateInn(iin)) {
            true -> checkUser(checkUserRequest!!)
            false -> Toast.makeText(
                this.context,
                getString(R.string.you_entered_is_incorrect),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkUser(checkUserRequest: CheckUserRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.checkUser(
                checkUserRequest = checkUserRequest
            )
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorAlertDialog()
        })
        viewModel.isCheckUser.observe(viewLifecycleOwner, {
            if (it != null) {
                setLoading(false)
                setLogin()
                initSpinner(it)
            } else errorAlertDialog()
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

    private fun errorAlertDialog() {
        setLoading(false)
        errorDialog(getString(R.string.error_unknown_body))
    }

    private fun initNavigation(phone: String) {
        val iin = et_enter_iin.text.toString()

        signUpRequest = SignUpRequest(iin = iin, phone = phone)

        bundle.putSerializable(CREATE_USER, signUpRequest)

        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_registrationFragment_to_smsFragment, bundle)
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
                btn_create.onClick {
                    initNavigation(checkResponse.data[position].phone.toString())
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        btn_create.isCheckable = !loading
        et_enter_iin.isEnabled = !loading
    }

    private fun setLogin() {
        tv_text_edit_phone.visibility = View.VISIBLE
        spinner_phone.visibility = View.VISIBLE
    }

}