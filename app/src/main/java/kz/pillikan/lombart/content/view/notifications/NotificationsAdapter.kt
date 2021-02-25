package kz.pillikan.lombart.content.view.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kz.pillikan.lombart.R
import kz.pillikan.lombart.content.model.response.notifications.NotificationsModel


class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {

    private val notificationList: ArrayList<NotificationsModel> = ArrayList()

    private var callback: NotificationsFragment

    constructor(callback: NotificationsFragment) : super() {
        this.callback = callback
    }

    fun addNotifications(notificationsList: List<NotificationsModel>) {
        this.notificationList.addAll(notificationsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationsAdapter.NotificationsViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notifications, parent, false)
        return NotificationsAdapter.NotificationsViewHolder(root)
    }

    override fun onBindViewHolder(
        holder: NotificationsViewHolder,
        position: Int
    ) {
        holder.bind(notificationList[position], callback)
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    class NotificationsViewHolder(private val root: View) : RecyclerView.ViewHolder(root) {

        private val tvHeading = root.findViewById(R.id.tv_heading) as TextView
        private val tvDescription = root.findViewById(R.id.tv_description) as TextView
        private val tvDate = root.findViewById(R.id.tv_date) as TextView

        fun bind(notificationsList: NotificationsModel, callback: NotificationsFragment) {
            tvDate.text = notificationsList.date
            tvDescription.text = notificationsList.description
            tvHeading.text = notificationsList.heading
        }
    }
}