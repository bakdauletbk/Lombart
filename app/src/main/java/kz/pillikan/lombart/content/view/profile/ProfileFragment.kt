package kz.pillikan.lombart.content.view.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_appeal.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.loadingView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseFragment
import kz.pillikan.lombart.content.model.response.home.ProfileInfo
import kz.pillikan.lombart.content.model.response.profile.CardModel
import kz.pillikan.lombart.content.viewmodel.profile.ProfileViewModel
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick

class ProfileFragment : BaseFragment() {

    private val adapter: CardAdapter = CardAdapter(this)

    private lateinit var viewModel: ProfileViewModel

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

    private fun init() {
        initViewModel()
        initRecyclerView()
        initNavigations()
        initUpdateFeed()
        initObservers()
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }


    private fun initNavigations() {
        tv_add_card.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_profileFragment_to_addCardFragment)
            }
        }
    }

    private fun initUpdateFeed() {
        setLoading(true)
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getCard()
        }
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getProfile()
        }
    }

    private fun initObservers() {
        viewModel.isError.observe(viewLifecycleOwner, Observer {
            errorDialog(getString(R.string.error_unknown_body))
        })
        viewModel.cardList.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                addCard(it)
            } else {
                errorDialog(getString(R.string.error_unknown_body))
            }
        })
        viewModel.profileInfo.observe(viewLifecycleOwner, {
            if (it != null) {
                setLoading(false)
                setProfileInfo(it)
            } else {
                errorDialog(getString(R.string.error_unknown_body))
            }
        })
    }

    private fun setProfileInfo(profileInfo: ProfileInfo) {
        tv_name.text = profileInfo.fio
        tv_number.text = profileInfo.phone
        tv_iin.text = profileInfo.iin
    }

    private fun addCard(cardList: List<CardModel>) {
        adapter.addCard(cardList)
    }

    private fun initRecyclerView() {
        rv_cards.adapter = adapter
        rv_cards.apply {
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun errorDialog(errorMsg: String) {
        activity?.alert {
            title = getString(R.string.error_unknown_title)
            message = errorMsg
            isCancelable = false
            positiveButton(getString(R.string.dialog_retry)) { dialog ->
                setLoading(false)
                dialog.dismiss()
                init()
            }
            negativeButton(getString(R.string.dialog_exit)) {
                activity?.finish()
            }
        }?.show()
    }

    private fun setLoading(loading: Boolean) {
        loadingView.visibility = if (loading) View.VISIBLE else View.GONE
        tv_add_card.isClickable = !loading
    }

}