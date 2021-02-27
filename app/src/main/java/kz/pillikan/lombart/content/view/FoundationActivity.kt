package kz.pillikan.lombart.content.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_foundation.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseActivity

class FoundationActivity : BaseActivity() {

    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foundation)
        lets()
    }

    private fun lets() {
        navigationListener()
    }

    private fun navigationListener() {
        val navController = findNavController(R.id.fragment2)
        bottom_navigation.setupWithNavController(navController)
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