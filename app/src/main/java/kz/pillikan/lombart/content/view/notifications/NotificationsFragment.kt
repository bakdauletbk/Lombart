package kz.pillikan.lombart.content.view.notifications

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.helpers.formatDateTime
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.common.views.PaginationScrollListener
import kz.pillikan.lombart.content.model.response.notifications.DataList
import kz.pillikan.lombart.content.viewmodel.notifications.NotificationsViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick

class NotificationsFragment : BaseFragment() {

    private val adapter: NotificationsAdapter = NotificationsAdapter(this)
    private var alertDialog: Dialog? = null
    private lateinit var viewModel: NotificationsViewModel
    private val intent = Intent()

    companion object {
        const val SHARE = "Share"
        const val TEXT_TYPE = "text/plain"
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
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initViewModel()
        initRecyclerView()
        initNatification()
        initObservers()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }

    private fun initNatification() {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getInitialPage()
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialogAlert()
        })
        viewModel.notificationList.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    addNotifications(it)
                    setLoading(false)
                }
                false -> setEmptyNotification()
            }
        })
    }

    private fun errorDialogAlert() {
        setLoading(false)
        errorDialog(getString(R.string.error_unknown_body))
    }

    private fun setEmptyNotification() {
        setLoading(false)
        rv_notifications.visibility = View.GONE
        tv_empty.visibility = View.VISIBLE
    }

    private fun addNotifications(notificationsList: List<DataList>) {
        adapter.addNotifications(notificationsList)
    }

    fun onAlertDialog(dataList: DataList) {
        alertDialog = Dialog(requireContext())
        alertDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        alertDialog!!.setContentView(R.layout.alert_dialog_notifications)

        val tvTitle: TextView = alertDialog!!.findViewById(R.id.tv_title)
        val tvDescription: TextView = alertDialog!!.findViewById(R.id.tv_notification)
        val btnShare: MaterialButton = alertDialog!!.findViewById(R.id.btn_share)

        tvTitle.text = dataList.title
        tvDescription.text = dataList.description

        val shareText = "${dataList.title}\n\n${dataList.description}\n\n${formatDateTime(dataList.created_at!!) + Constants.YEARS}"

        btnShare.onClick {
            intent.action = Intent.ACTION_SEND
            intent.type = TEXT_TYPE
            intent.putExtra(
                Intent.EXTRA_TEXT,
                shareText
            )
            startActivity(Intent.createChooser(intent, SHARE))
        }

        alertDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.show()
    }

    private fun initRecyclerView() {
        rv_notifications.adapter = adapter
        rv_notifications.addOnScrollListener(object :
            PaginationScrollListener(rv_notifications.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return viewModel.isHasNext()
            }

            override fun isLoading(): Boolean {
                return viewModel.isLoading()
            }

            override fun loadMoreItems() {
                setLoading(true)
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.getNextPage()
                }
            }
        })
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }

}