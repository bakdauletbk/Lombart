package kz.pillikan.lombart.content.model.repository.notifications

import android.app.Application
import kz.pillikan.lombart.content.model.response.notifications.NotificationsModel

class NotificationsRepository(application: Application) {

    suspend fun getNotifications(): List<NotificationsModel?> {
        return try {
            val notifications: ArrayList<NotificationsModel> = ArrayList()
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim  asdasdasdas dasd asd sadd ", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim asdas dsa das dasda asd51 ad", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim dasdassadasdasd  asda  uguy gu ygu yg ug yug uyyg uy ", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            notifications.add(NotificationsModel("Sert Lombard", "Sert Lombard Zaim", "01.02.2021"))
            return notifications
        } catch (e: Exception){
            val notifications: ArrayList<NotificationsModel> = ArrayList()
            notifications.clear()
            return notifications
        }
    }

}