package kz.pillikan.lombart.content.view.profile

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add_cards.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseFragment

class AddCardFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): AddCardFragment {
            return AddCardFragment()
        }
    }

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
    }
}