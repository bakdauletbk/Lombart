package kz.pillikan.lombart.splash

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.view.AuthorizationActivity
import kz.pillikan.lombart.common.views.BaseActivity
import kz.pillikan.lombart.content.view.FoundationActivity
import org.jetbrains.anko.alert
import org.jetbrains.anko.intentFor

class SplashActivity : BaseActivity() {

    private lateinit var viewModel: SplashViewModel
    private var isFirstLaunch = true
    private val mContext = this
    private var notificationId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkNotificationExtra()
        lets()
        observer()
    }

    private fun checkNotificationExtra() {
        notificationId = intent.getIntExtra("NOTIFICATION_ID", 0)
    }

    private fun lets() {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            checkNetwork()
        }
    }

    private fun observer() {
        viewModel.isNetworkConnection.observe(mContext, {
            if (it) {
                checkAuthorize()
            } else {
                showErrorDialog(
                    getString(R.string.error_no_internet_title),
                    getString(R.string.error_no_internet_msg)
                )
            }
        })

        viewModel.isAuthorize.observe(mContext, {
            if (it) {
                if (notificationId != null){
                    startActivity(intentFor<FoundationActivity>().putExtra("NOTIFICATION_ID", notificationId))
                    finish()
                }else{
                    startActivity(intentFor<FoundationActivity>())
                    finish()
                }
            } else {
                startActivity(intentFor<AuthorizationActivity>())
                finish()
            }
        })

        viewModel.isError.observe(mContext, {
            if (it) {
                showErrorDialog(
                    getString(R.string.error_unknown_title),
                    getString(R.string.error_unknown_body)
                )
            }
        })

    }

    private suspend fun checkNetwork() {
        if (isFirstLaunch) {
            delay(2000L)
            isFirstLaunch = false
        }
        viewModel.checkNetwork(mContext)
    }

    private fun checkAuthorize() {
        viewModel.checkAuthorize()
    }

    private fun showErrorDialog(errorTitle: String, errorMessage: String) {
        alert {
            title = errorTitle
            message = errorMessage
            isCancelable = false
            negativeButton(getString(R.string.dialog_exit)) {
                finish()
            }
            positiveButton(getString(R.string.dialog_retry)) {
                it.dismiss()
                onRestart()
            }
        }.show()
    }

    override fun onRestart() {
        super.onRestart()
        CoroutineScope(Dispatchers.IO).launch {
            checkNetwork()
        }
    }

}