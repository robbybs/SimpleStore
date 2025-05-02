package com.rbs.simplestore.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rbs.simplestore.databinding.BottomSheetProfileBinding
import com.rbs.simplestore.domain.model.UserDomain

class ProfileBottomSheet(
    private val data: UserDomain
) : BottomSheetDialogFragment() {
    private val binding by lazy { BottomSheetProfileBinding.inflate(layoutInflater) }
    private var setOnLogout: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeUI()
    }

    private fun subscribeUI() {
        with(binding) {
            data.apply {
                tvName.text = name
                tvEmail.text = email
                btnLogout.setOnClickListener { setOnLogout?.invoke() }
            }
        }
    }

    fun setOnLogout(listener: () -> Unit) {
        setOnLogout = listener
    }

    companion object {
        private const val TAG: String = "ProfileBottomSheet"

        @JvmStatic
        fun showDialog(fragmentManager: FragmentManager, data: UserDomain): ProfileBottomSheet {
            val dialog = ProfileBottomSheet(data)

            if (fragmentManager.findFragmentByTag(TAG) == null) {
                dialog.show(fragmentManager, TAG)
            }

            return dialog
        }
    }
}