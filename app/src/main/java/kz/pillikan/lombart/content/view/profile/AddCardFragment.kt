package kz.pillikan.lombart.content.view.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add_cards.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.view.AuthorizationActivity
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.request.profile.CardRequest
import kz.pillikan.lombart.content.model.response.profile.CardResponse
import kz.pillikan.lombart.content.view.epay.EpayFragment
import kz.pillikan.lombart.content.viewmodel.profile.AddCardViewModel
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor

class AddCardFragment : BaseFragment() {

    private lateinit var viewModel: AddCardViewModel
    private val bundle = Bundle()
    private var epayFragment: EpayFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.profileFragment)
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar_add_card.setNavigationIcon(R.drawable.ic_arrow_black)
        toolbar_add_card.setTitleTextColor(Color.BLACK)
        toolbar_add_card.title = getString(R.string.add_cards)
        toolbar_add_card.setNavigationOnClickListener {
            view?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.profileFragment) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_cards, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lets()
    }

    private fun lets() {
        initViewModel()
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.cardResponse.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> {
                    sendEpay(it)
                }
                false -> errorDialog(getString(R.string.error_failed_connection_to_server))
            }
        })
        viewModel.isUpdateApp.observe(viewLifecycleOwner, {
            when (it) {
                true ->
                    showAlertDialog(
                        requireContext(),
                        getString(R.string.our_application_has_been_updated_please_update)
                    )
            }
        })
        viewModel.isUnAuthorized.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    setLoading(false)
                    viewModel.clearSharedPref()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.you_are_logged_in_under_your_account_on_another_device),
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(intentFor<AuthorizationActivity>())
                    activity?.finish()
                }
            }
        })
    }

    private fun sendEpay(cardResponse: CardResponse) {
        bundle.putSerializable(Constants.PUT_EXTRA_CARD_RESPONSE, cardResponse)

        view?.let { it1 ->
            Navigation.findNavController(it1)
                .navigate(R.id.action_addCardFragment_to_epayFragment, bundle)
        }

    }

    private fun initListeners() {
        btn_add_card.onClick {
            prepareCard()
        }
    }

    private fun prepareCard() {
        val cardName = et_card_name.text.toString()

        val cardRequest = CardRequest(cardName = base64encode(cardName))
        when (cardName.isNotEmpty()) {
            true -> {
                sendCardName(cardRequest)
            }
            false -> Toast.makeText(
                requireContext(),
                getString(R.string.edit_card_name),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun sendCardName(cardRequest: CardRequest) {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.addCard(cardRequest)
        }
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[AddCardViewModel::class.java]
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
    }
}