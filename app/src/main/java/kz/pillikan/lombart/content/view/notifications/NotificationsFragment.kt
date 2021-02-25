package kz.pillikan.lombart.content.view.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.notifications.NotificationsModel
import kz.pillikan.lombart.content.viewmodel.notifications.NotificationsViewModel
import org.jetbrains.anko.alert

class NotificationsFragment : BaseFragment() {

    private val adapter: NotificationsAdapter = NotificationsAdapter(this)

    private lateinit var viewModel: NotificationsViewModel


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
        initNotificationsList()
        initObservers()
    }

    private fun initNotificationsList() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getNotifications()
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner,  {
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.notificationList.observe(viewLifecycleOwner,  {
            if (it != null) {
                addNotifications(it)
            } else {
                errorDialog(getString(R.string.error_unknown_body))
            }
        })
    }

    private fun addNotifications(notificationsList: List<NotificationsModel>) {
        adapter.addNotifications(notificationsList)
    }

    private fun initRecyclerView() {
        rv_notifications.adapter = adapter
        rv_notifications.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
    }


    private fun errorDialog(errorMsg: String) {
        activity?.alert {
            title = getString(R.string.error_unknown_title)
            message = errorMsg
            isCancelable = false
            positiveButton(getString(R.string.dialog_retry)) { dialog ->
                dialog.dismiss()
                init()
            }
            negativeButton(getString(R.string.dialog_exit)) {
                activity?.finish()
            }
        }?.show()
    }
}