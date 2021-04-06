package kz.pillikan.lombart.content.view.appeal

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_appeal.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.Validators
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest
import kz.pillikan.lombart.content.model.response.appeal.ResponseAdvancedData
import kz.pillikan.lombart.content.viewmodel.appeal.AppealViewModel
import org.jetbrains.anko.sdk27.coroutines.onClick

class AppealFragment : BaseFragment() {

    private lateinit var viewModel: AppealViewModel

    companion object {
        const val TAG = "AppealFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
        }
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
        updateFeed()
        initListeners()
        initObservers()
    }

    private fun updateFeed() {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getAdvancedData()
        }
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
        viewModel.advancedData.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    setAdvancedData(it)
                }
                false -> {
                    setLoading(false)
                    errorDialog(getString(R.string.error_failed_connection_to_server))
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setAdvancedData(advancedData: ResponseAdvancedData) {
        setLoading(false)
        tv_address.text = advancedData.address
        tv_phone.text = advancedData.phone
        tv_mail.text = advancedData.email

        tv_appeal.text =
            getString(R.string.appeal_text) + advancedData.phone + getString(R.string.appeal_text_end)

        ll_phone.onClick {
            call(advancedData.phone.toString())
        }
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