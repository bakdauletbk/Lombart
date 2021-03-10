package kz.pillikan.lombart.content.view.appeal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_appeal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest
import kz.pillikan.lombart.content.viewmodel.appeal.AppealViewModel
import org.jetbrains.anko.sdk27.coroutines.onClick

class AppealFragment : BaseFragment() {

    private lateinit var viewModel: AppealViewModel

    companion object {
        const val TAG = "AppealFragment"
    }

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
        setData()
    }

    private fun setData() {
        tv_address.text = Constants.ADDRESS
        tv_phone.text = Constants.PHONE
        tv_mail.text = Constants.MAIL
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(AppealViewModel::class.java)
    }

    private fun initListeners() {
        btn_send.onClick {
            prepareFeedback()
        }
        ll_phone.onClick {
            call(Constants.PHONE)
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
                getString(R.string.data_you_entered_is_incorrect),
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
            setLoading(false)
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.isSuccess.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                    Toast.makeText(
                        context,
                        getString(R.string.your_message_has_been_sent), Toast.LENGTH_LONG
                    ).show()
                }
                false -> {
                    setLoading(false)
                    Toast.makeText(
                        context,
                        getString(R.string.your_appeal_has_not_been_sent), Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
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