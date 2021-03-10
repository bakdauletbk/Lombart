package kz.pillikan.lombart.content.view.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add_cards.*
import kotlinx.android.synthetic.main.fragment_change_password.*
import kotlinx.android.synthetic.main.fragment_change_password.loadingView
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.request.profile.ChangePassword
import kz.pillikan.lombart.content.model.request.profile.CheckPassword
import kz.pillikan.lombart.content.viewmodel.profile.ChangePasswordViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick

class ChangePassword : BaseFragment() {

    private lateinit var viewModel: ChangePasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_black)
        toolbar.setTitleTextColor(Color.BLACK)
        toolbar.setNavigationOnClickListener {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_changePassword_to_profileFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
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
        viewModel = ViewModelProvider(this).get(ChangePasswordViewModel::class.java)
    }

    private fun initListeners() {
        btn_save.onClick {
            preparePassword()
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialogAlert()
        })
        viewModel.isCheckPassword.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                    showChangePassword()
                    prepareChangePassword()
                }
                false -> errorDialogAlert()
            }
        })
        viewModel.isChangePassword.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    Toast.makeText(
                        context,
                        getString(R.string.password_changed_successfully),
                        Toast.LENGTH_LONG
                    ).show()
                    view?.let { it1 ->
                        Navigation.findNavController(it1)
                            .navigate(R.id.action_changePassword_to_profileFragment)
                    }
                }
                false -> errorDialogAlert()
            }
        })
    }

    private fun errorDialogAlert(){
        setLoading(false)
        errorDialog(getString(R.string.error_unknown_body))
    }

    private fun prepareChangePassword() {
        val password = et_password1.text.toString()
        val password2 = et_password2.text.toString()

        val passwordBase64 = base64encode(password)
        val password2Base64 = base64encode(password2)

        val changePassword = ChangePassword(password = passwordBase64, password2 = password2Base64)

        when (Validators.validatePassword(password) && Validators.validatePassword(password2) && password == password2) {
            true -> setChangePassword(changePassword)
            false -> Toast.makeText(context, getString(R.string.password_edit), Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun setChangePassword(changePassword: ChangePassword) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.changePassword(changePassword)
        }
    }

    private fun showChangePassword() {
        ll_create_password.visibility = View.VISIBLE
        btn_save.text = getString(R.string.save)
        ll_current_password.visibility = View.GONE
    }

    private fun preparePassword() {
        val password = et_password.text.toString()
        val passwordBase64 = base64encode(password)

        val checkPassword = CheckPassword(password = passwordBase64)
        when (Validators.validatePassword(password)) {
            true -> setCheckPassword(checkPassword)
            false -> Toast.makeText(context, getString(R.string.password_edit), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun setCheckPassword(checkPassword: CheckPassword) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.checkPassword(checkPassword)
        }
    }


    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }

}