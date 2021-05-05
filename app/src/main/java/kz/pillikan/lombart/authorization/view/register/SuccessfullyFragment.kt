package kz.pillikan.lombart.authorization.view.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_successfully.*
import kz.pillikan.lombart.R
import kz.pillikan.lombart.common.views.BaseFragment
import org.jetbrains.anko.sdk27.coroutines.onClick

class SuccessfullyFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_successfully, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        navigationListener()
    }

    private fun navigationListener() {
        btn_proceed.onClick {
            view?.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_successfullyFragment_to_pinCodeFragment)
            }
        }
    }

}