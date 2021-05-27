package kz.pillikan.lombart.content.view.profile

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.loadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.view.AuthorizationActivity
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.home.CardList
import kz.pillikan.lombart.content.model.response.home.ProfileInfo
import kz.pillikan.lombart.content.viewmodel.profile.ProfileViewModel
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.intentFor

class ProfileFragment : BaseFragment() {

    private val adapter: CardAdapter = CardAdapter(this)

    private lateinit var viewModel: ProfileViewModel

    private var isOtherAuthShowOnce = false
    private var isUpdateAppShowOnce = false
    private var isErrorShowOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun init() {
        initViewModel()
        initRecyclerView()
        initNavigation()
        initToolbar()
        updateFeed()
        initObservers()
    }

    private fun initToolbar() {
        toolbars.inflateMenu(R.menu.menu_toolbar)
        toolbars.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_exit -> viewModel.logout()
            }
            true
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    private fun initNavigation() {
        ll_reset_pin.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_profileFragment_to_pinCodeFragment2)
            }
        }
        ll_reset_password.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(
                        R.id.action_profileFragment_to_changePassword
                    )
            }
        }
        tv_add_card.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_profileFragment_to_addCardFragment)
            }
        }
    }

    private fun updateFeed() {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getCard()
        }
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getProfile()
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, {
            if (!isErrorShowOnce) {
                errorDialogAlert()
                isErrorShowOnce = true
            }
        })
        viewModel.cardList.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> addCard(it)
                false -> showEmptyCard()
            }
        })
        viewModel.profileInfo.observe(viewLifecycleOwner, {
            when (it != null) {
                true -> setProfileInfo(it)
                false -> errorDialogAlert()
            }
        })
        viewModel.isLogout.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    startActivity(intentFor<AuthorizationActivity>())
                    activity?.finish()
                }
                false -> {
                    setLoading(false)
                    Toast.makeText(
                        context,
                        getString(R.string.an_error_occurred_while_logging_out),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
        viewModel.isUpdateApp.observe(viewLifecycleOwner, {
            when (it) {
                true ->
                    if (!isUpdateAppShowOnce) {
                        showAlertDialog(
                            requireContext(),
                            getString(R.string.our_application_has_been_updated_please_update)
                        )
                        isUpdateAppShowOnce = true
                    }
            }
        })
        viewModel.isUnAuthorized.observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    if (!isOtherAuthShowOnce) {
                        setLoading(false)
                        viewModel.clearSharedPref()
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.you_are_logged_in_under_your_account_on_another_device),
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(intentFor<AuthorizationActivity>())
                        isOtherAuthShowOnce = true
                        activity?.finish()
                    }
                }
            }
        })

    }

    private fun showEmptyCard() {
        setLoading(false)
        tv_empty_card.visibility = View.VISIBLE
        rv_cards.visibility = View.GONE
    }

    private fun errorDialogAlert() {
        setLoading(false)
        errorDialog(getString(R.string.error_unknown_body))
    }

    private fun setProfileInfo(profileInfo: ProfileInfo) {
        setLoading(false)
        tv_name.text = profileInfo.fio
        tv_number.text = profileInfo.phone
        tv_iin.text = profileInfo.iin
    }

    private fun addCard(cardList: List<CardList>) {
        setLoading(false)
        when(cardList.size >= Constants.ONE){
            false->showEmptyCard()
        }
        adapter.addCard(cardList)
    }

    private fun initRecyclerView() {
        rv_cards.adapter = adapter
        rv_cards.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        tv_add_card.isClickable = !loading
    }

}