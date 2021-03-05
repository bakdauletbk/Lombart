package kz.pillikan.lombart.content.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.yandex.mapkit.MapKitFactory
import kotlinx.android.synthetic.main.activity_foundation.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.views.BaseActivity

class FoundationActivity : BaseActivity() {

    companion object {
        const val HOME = 0
        const val ABOUT = 1
        const val PROFILE = 2
        const val NOTIFICATION = 3
        const val APPEAL = 4
    }

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
    }

    private fun destinationListeners(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.profileFragment -> {
                    switchMenuItem(PROFILE)
                    showBottomNavigation(navController)
                }
                R.id.homeFragment -> {
                    switchMenuItem(HOME)
                    showBottomNavigation(navController)
                }
                R.id.notificationsFragment -> {
                    switchMenuItem(NOTIFICATION)
                    showBottomNavigation(navController)
                }
                R.id.appealFragment -> {
                    switchMenuItem(APPEAL)
                    showBottomNavigation(navController)
                }
                R.id.aboutFragment -> {
                    switchMenuItem(ABOUT)
                    showBottomNavigation(navController)
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

    private fun showBottomNavigation(navController: NavController) {
        bottom_navigation.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        if (exit) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        } else {
            Toast.makeText(
                this, "Для выхода снова нажмите «Назад».",
                Toast.LENGTH_SHORT
            ).show()
            exit = true
        }
    }
}