package kz.pillikan.lombart.content.view.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_notifications.loadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.common.views.PaginationScrollListener
import kz.pillikan.lombart.content.model.response.notifications.DataList
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
        initRecyclerView()
        initViewModel()
        initNatificaionList()
        initObservers()
    }

    private fun initNatificaionList() {
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

    private fun initRecyclerView() {
        rv_notifications.adapter = adapter
        val paginationScrollListener =
            object :
                PaginationScrollListener(rv_notifications.layoutManager as LinearLayoutManager) {

                override fun isLastPage(): Boolean {
                    return viewModel.isLastPage()
                }

                override fun isLoading(): Boolean {
                    return viewModel.isLoading()
                }

                override fun loadMoreItems() {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.getNextPage()
                    }
                }
            }
        rv_notifications.addOnScrollListener(paginationScrollListener)
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

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }


}