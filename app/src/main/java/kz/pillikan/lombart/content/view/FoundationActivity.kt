package kz.pillikan.lombart.content.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_foundation.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.views.BaseActivity

class FoundationActivity : BaseActivity() {

    companion object {
        const val HOME = 0
        const val ABOUT = 1
        const val PROFILE = 2
        const val NOTIFICATION = 3
        const val APPEAL = 4

        const val NOTIFICATION_ID = "NOTIFICATION_ID"
        const val DEFAULT_VALUE = 0
    }

    private var currentNavigationItemId = Constants.ZERO

    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foundation)
        lets()
    }

    private fun lets() {
        val navController = findNavController(R.id.fragment2)
        setupNavController(navController)
        destinationListeners(navController)
        initBottomNavigation()
        checkExtraNotificationId()
    }

    private fun initBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener { item ->
            if (item.itemId != currentNavigationItemId) {
                when (item.itemId) {
                    R.id.menu_home -> showHomeFragment()
                    R.id.menu_about -> showAboutFragment()
                    R.id.menu_profile -> showProfileFragment()
                    R.id.menu_notification -> showNotification()
                    R.id.menu_appeal -> showAppealFragment()
                }
            }
            true
        }

    }

    fun navigateToAppeal() {
        bottom_navigation.selectedItemId = R.id.menu_appeal
    }

    private fun showAppealFragment() {
        currentNavigationItemId = R.id.menu_appeal
        findNavController(R.id.fragment2)
            .navigate(R.id.appealFragment)
    }

    private fun showNotification() {
        currentNavigationItemId = R.id.menu_notification
        findNavController(R.id.fragment2)
            .navigate(R.id.notificationsFragment)
    }

    private fun showProfileFragment() {
        currentNavigationItemId = R.id.menu_profile
        findNavController(R.id.fragment2)
            .navigate(R.id.profileFragment)
    }

    private fun showAboutFragment() {
        currentNavigationItemId = R.id.menu_about
        findNavController(R.id.fragment2)
            .navigate(R.id.aboutFragment)
    }

    private fun showHomeFragment() {
        currentNavigationItemId = R.id.menu_home
        findNavController(R.id.fragment2)
            .navigate(R.id.homeFragment)
    }

    private fun checkExtraNotificationId() {
        val notificationId = intent.getIntExtra(NOTIFICATION_ID, DEFAULT_VALUE)
        if (notificationId > Constants.ZERO) {
            findNavController(R.id.fragment2)
                .navigate(R.id.action_homeFragment_to_notificationsFragment)
        }
    }

    private fun destinationListeners(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.profileFragment -> {
                    switchMenuItem(PROFILE)
                    showBottomNavigation()
                }
                R.id.homeFragment -> {
                    switchMenuItem(HOME)
                    showBottomNavigation()
                    checkExtraNotificationId()
                }
                R.id.notificationsFragment -> {
                    switchMenuItem(NOTIFICATION)
                    showBottomNavigation()
                }
                R.id.appealFragment -> {
                    switchMenuItem(APPEAL)
                    showBottomNavigation()
                }
                R.id.aboutFragment -> {
                    switchMenuItem(ABOUT)
                    showBottomNavigation()
                }
                else -> hideBottomNavigation()
            }
        }
    }

    private fun setupNavController(navController: NavController) {
        bottom_navigation.itemIconTintList = null
        bottom_navigation.setupWithNavController(navController)
    }

    private fun switchMenuItem(index: Int, enabled: Boolean = true) {
        bottom_navigation.menu.getItem(index).isEnabled = enabled
    }

    private fun hideBottomNavigation() {
        bottom_navigation.visibility = View.GONE
    }

    private fun showBottomNavigation() {
        bottom_navigation.visibility = View.VISIBLE
    }

}