package kz.pillikan.lombart.content.view.notifications

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.common.views.PaginationScrollListener
import kz.pillikan.lombart.common.views.SoftPaginationScrollListener
import kz.pillikan.lombart.content.model.response.notifications.DataList
import kz.pillikan.lombart.content.viewmodel.notifications.NotificationsViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick

class NotificationsFragment : BaseFragment() {

    private val adapter: NotificationsAdapter = NotificationsAdapter(this)
    private var alertDialog: Dialog? = null
    private lateinit var viewModel: NotificationsViewModel
    private val intent = Intent()
    private var isDialogVisibility = false

    companion object {
        const val SHARE = "Share"
        const val TEXT_TYPE = "text/plain"
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
        initRecyclerView()
        initViewModel()
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
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.notificationList.observe(viewLifecycleOwner, {
            if (it != null) {
                addNotifications(it)
                setLoading(false)
            } else {
                setEmptyNotification()
                errorDialog(getString(R.string.error_unknown_body))
            }
        })
    }

    private fun setEmptyNotification() {
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

        btnShare.onClick {
            intent.action = Intent.ACTION_SEND
            intent.type = TEXT_TYPE
            intent.putExtra(
                Intent.EXTRA_TEXT,
                dataList.description
            )
            startActivity(Intent.createChooser(intent, SHARE))
        }

        alertDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.show()
    }

    private fun initRecyclerView() {
        rv_notifications.adapter = adapter
        rv_notifications.addOnScrollListener(object :
            SoftPaginationScrollListener(rv_notifications.layoutManager as LinearLayoutManager) {

            override fun isLastPage(): Boolean {
                return viewModel.isHasNext()
            }

            override fun isLoading(): Boolean {
                return  viewModel.isLoading()
            }

             override fun loadMoreItems() {
                 CoroutineScope(Dispatchers.IO).launch {
                     viewModel.getNextPage()
                 }
            }
        })

    }

    private fun errorDialog(errorMsg: String) {
        if (!isDialogVisibility) {
            isDialogVisibility = true
            activity?.alert {
                title = getString(R.string.error_unknown_title)
                message = errorMsg
                isCancelable = false
                positiveButton(getString(R.string.dialog_ok)) { dialog ->
                    dialog.dismiss()
                    isDialogVisibility = false
                }
            }?.show()
        }
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }


}