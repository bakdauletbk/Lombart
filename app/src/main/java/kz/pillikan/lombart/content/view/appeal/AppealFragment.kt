package kz.pillikan.lombart.content.view.appeal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_appeal.*
import kotlinx.android.synthetic.main.fragment_appeal.loadingView
import kotlinx.android.synthetic.main.fragment_create_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest
import kz.pillikan.lombart.content.viewmodel.appeal.AppealViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick

class AppealFragment : BaseFragment() {

    private lateinit var viewModel: AppealViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appeal, container, false)
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
        viewModel = ViewModelProvider(this).get(AppealViewModel::class.java)
    }

    private fun initListeners() {
        btn_send.onClick {
            prepareFeedback()
        }
    }

    private fun prepareFeedback() {
        val name = et_name.text.toString()
        val description = et_description.text.toString()

        val feedbackRequest = FeedbackRequest(name = name, description = description)

        when (Validators.validateIsNotEmpty(name) && Validators.validateIsNotEmpty(description)) {
            true -> sendFeedback(feedbackRequest)
            false -> Toast.makeText(
                context,
                "Введенные вами данные некорректны!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun sendFeedback(feedbackRequest: FeedbackRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.sendFeedback(feedbackRequest)
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.isSuccess.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                    Toast.makeText(
                        context,
                        "Спасибо! Ваше обращение отправлено!", Toast.LENGTH_LONG
                    ).show()
                }
                false -> {
                    setLoading(false)
                    Toast.makeText(
                        context,
                        "К Сожалению! Ваше обращение не отправлено", Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }

    private fun errorDialog(errorMsg: String) {
        activity?.alert {
            title = getString(R.string.error_unknown_title)
            message = errorMsg
            isCancelable = false
            positiveButton(getString(R.string.dialog_ok)) { dialog ->
                setLoading(false)
                dialog.dismiss()
            }
        }?.show()
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        btn_send.isCheckable = !loading
        et_name.isEnabled = !loading
        et_description.isEnabled = !loading
        ll_phone.isClickable = !loading
        ll_mail.isClickable = !loading
    }

}