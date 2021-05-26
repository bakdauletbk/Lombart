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
import kotlinx.android.synthetic.main.fragment_epay.view.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.utils.EpayCallback
import kz.pillikan.lombart.common.utils.EpayConstants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.profile.CardResponse
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.net.URLEncoder


class EpayFragment : BaseFragment() {

    private var webSettings: WebSettings? = null

    private var successCallback: EpayCallback? = null
    private var failureCallback: EpayCallback? = null

    private var postUrl: String? = null

    private var useTestMode = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_epay, container, false)
        val webView = view.findViewById<View>(R.id.web_view) as WebView

        val webSettings: WebSettings = webView.getSettings()
        webSettings.javaScriptEnabled = true

        webView.setWebViewClient(WebViewClient())

        webView.setWebViewClient(
            object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    Log.e(EpayConstants.LOG_TAG, "URL loading = $url")
                    return if (EpayConstants.EXTRA_POST_LINK_VALUE.equals(url)) {
                        successCallback!!.process(url)
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        true
                    } else if (EpayConstants.EPAY_FAILURE_BACK_LINK.equals(url)) {
                        failureCallback!!.process(url)
                        Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
                        true
                    } else {
                        false
                    }
                }
            }
        )

        Log.d("Ermahan", buildPostData()?.toByteArray().toString())
        postUrl?.let { view.web_view.postUrl(it, buildPostData()!!.toByteArray()) }

        return view

    }

    private fun buildPostData(): String? {
        val cardResponse = arguments?.getSerializable(
            Constants.PUT_EXTRA_CARD_RESPONSE
        ) as CardResponse

        postUrl = cardResponse.url
        Log.d("PostUrl", postUrl.toString())
        var postData = ""
        Log.e(EpayConstants.LOG_TAG, "signed = ${cardResponse.Signed_Order_B64}")

        postData = (URLEncoder.encode("Signed_Order_B64", "UTF-8") + "=" + URLEncoder.encode(
            cardResponse.Signed_Order_B64, "UTF-8"
        ) + "&" + URLEncoder.encode("template", "UTF-8") + "=" + URLEncoder.encode(
            cardResponse.template, "UTF-8"
        ) + "&" + URLEncoder.encode("Language", "UTF-8") + "=" + URLEncoder.encode(
            cardResponse.Language, "UTF-8"
        ) + "&" + URLEncoder.encode(
            "BackLink", "UTF-8"
        ) + "=" + URLEncoder.encode(cardResponse.BackLink, "UTF-8") + "&" + URLEncoder.encode(
            "FailureBackLink", "UTF-8"
        ) + "=" + URLEncoder.encode(
            cardResponse.FailureBackLink, "UTF-8"
        ) + "&" + URLEncoder.encode(
            "FailurePostLink", "UTF-8"
        ) + "=" + URLEncoder.encode(
            cardResponse.FailureBackLink, "UTF-8"
        ) + "&" + URLEncoder.encode("PostLink", "UTF-8") + "=" + URLEncoder.encode(
            cardResponse.PostLink, "UTF-8"
        ) + "&" + URLEncoder.encode(
            "email", "UTF-8"
        ) + "=" + URLEncoder.encode(cardResponse.email, "UTF-8"))


        return postData

    }

}