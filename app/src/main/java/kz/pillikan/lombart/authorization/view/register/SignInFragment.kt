package kz.pillikan.lombart.authorization.view.register

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_sigin.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.request.SignInRequest
import kz.pillikan.lombart.authorization.viewmodel.register.SignInViewModel
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.view.FoundationActivity
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.intentFor

class SignInFragment : BaseFragment() {

    private lateinit var viewModel: SignInViewModel
    private var signInRequest: SignInRequest? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sigin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lets()
    }

    private fun lets() {
        initViewModel()
        initListeners()
        initObservers()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.isSuccess.observe(viewLifecycleOwner, {
            if (it) {
                startActivity(intentFor<FoundationActivity>())
            } else {
                onSuccessFullDialog()
            }
        })
    }

    private fun prepareLogin() {
        val iin = et_iin.text.toString()
        val password = et_password.text.toString()
        val fToken = "sakfoas"

        //Base64 encode
        val iinBase64 = base64encode(iin)
        val passwordBase64 = base64encode(password)
        val fTokenBase64 = base64encode(fToken)

        signInRequest = SignInRequest(
            iin = iinBase64,
            ftoken = fTokenBase64,
            password = passwordBase64
        )

        when (Validators.validateInn(iin) && Validators.validatePassword(password)) {
            true -> signIn(signInRequest!!)
            false -> Toast.makeText(
                this.context,
                "Введенные вами данные некорректны!",
                Toast.LENGTH_LONG
            )
                .show()
        }

    }

    private fun signIn(signInRequest: SignInRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.signIn(signInRequest)
        }
    }

    private fun initListeners() {
        btn_to_come_in.onClick { prepareLogin() }
        tv_remember_password.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_signInFragment_to_passwordRecoveryFragment)
            }
        }
        ll_sign_up.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_signInFragment_to_registrationFragment)
            }
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
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        btn_to_come_in.isCheckable = !loading
        et_iin.isEnabled = !loading
        et_password.isEnabled = !loading
    }

}