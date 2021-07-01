package kz.pillikan.lombart.content.view.epay

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_epay.view.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.utils.EpayCallback
import kz.pillikan.lombart.common.utils.EpayConstants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.profile.CardResponse
import java.net.URLEncoder

class EpayFragment : BaseFragment() {

    private var successCallback: EpayCallback? = null
    private var failureCallback: EpayCallback? = null
    private var postUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.addCardFragment)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_epay, container, false)
        val webView = view.findViewById<View>(R.id.web_view) as WebView

        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        webView.webViewClient = WebViewClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                Log.e(EpayConstants.LOG_TAG, "URL loading = $url")

                return when (url) {
                    EpayConstants.EXTRA_POST_LINK_VALUE -> {
                        successCallback!!.process(url)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.map_added_successfully),
                            Toast.LENGTH_LONG
                        ).show()
                        view.let { it1 ->
                            Navigation.findNavController(it1)
                                .navigate(R.id.addCardFragment)
                        }
                        true
                    }
                    EpayConstants.EPAY_FAILURE_BACK_LINK -> {
                        failureCallback!!.process(url)
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.an_error_occurred_while_adding_a_card),
                            Toast.LENGTH_LONG
                        ).show()
                        true
                    }
                    else -> false
                }
            }
        }

        Log.d(
            "Ermahan",
            buildPostData()?.toByteArray().toString()
        ) // Не Уберать изза этого не откроется сайт  <---------->
        postUrl?.let { view.web_view.postUrl(it, buildPostData()!!.toByteArray()) }
        return view
    }

    private fun buildPostData(): String? {
        val cardResponse = arguments?.getSerializable(
            Constants.PUT_EXTRA_CARD_RESPONSE
        ) as CardResponse

        postUrl = cardResponse.url

        return (URLEncoder.encode(
            Constants.Signed_Order_B64,
            Constants.UTF_8
        ) + Constants.equally + URLEncoder.encode(
            cardResponse.Signed_Order_B64, Constants.UTF_8
        ) + Constants.end + URLEncoder.encode(
            Constants.template,
            Constants.UTF_8
        ) + Constants.equally + URLEncoder.encode(
            cardResponse.template, Constants.UTF_8
        ) + Constants.end + URLEncoder.encode(
            Constants.Language,
            Constants.UTF_8
        ) + Constants.equally + URLEncoder.encode(
            cardResponse.Language, Constants.UTF_8
        ) + Constants.end + URLEncoder.encode(
            Constants.BackLink, Constants.UTF_8
        ) + Constants.equally + URLEncoder.encode(
            cardResponse.BackLink,
            Constants.UTF_8
        ) + Constants.end + URLEncoder.encode(
            Constants.FailureBackLink, Constants.UTF_8
        ) + Constants.equally + URLEncoder.encode(
            cardResponse.FailureBackLink, Constants.UTF_8
        ) + Constants.end + URLEncoder.encode(
            Constants.FailurePostLink, Constants.UTF_8
        ) + Constants.equally + URLEncoder.encode(
            cardResponse.FailureBackLink, Constants.UTF_8
        ) + Constants.end + URLEncoder.encode(
            Constants.PostLink,
            Constants.UTF_8
        ) + Constants.equally + URLEncoder.encode(
            cardResponse.PostLink, Constants.UTF_8
        ) + Constants.end + URLEncoder.encode(
            Constants.email, Constants.UTF_8
        ) + Constants.equally + URLEncoder.encode(cardResponse.email, Constants.UTF_8))
    }

}