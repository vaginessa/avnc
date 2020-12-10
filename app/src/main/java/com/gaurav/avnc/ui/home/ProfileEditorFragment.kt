/*
 * Copyright (c) 2020  Gaurav Ujjwal.
 *
 * SPDX-License-Identifier:  GPL-3.0-or-later
 *
 * See COPYING.txt for more details.
 */

package com.gaurav.avnc.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.gaurav.avnc.R
import com.gaurav.avnc.databinding.FragmentProfileEditorBinding
import com.gaurav.avnc.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * Fragment used for creating/editing server profiles.
 */
class ProfileEditorFragment : DialogFragment() {

    private val viewModel by activityViewModels<HomeViewModel>()

    /**
     * Target of this dialog.
     *
     * Instead of directly modifying profile in [HomeViewModel.profileEditEvent],
     * we make a deep copy. This avoids some issues with adapters not noticing
     * the change in database .
     */
    private val profile by lazy { viewModel.profileEditEvent.value!!.copy() }

    /**
     * Creates editor dialog.
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = FragmentProfileEditorBinding.inflate(layoutInflater, null, false)
        binding.profile = profile

        val dialog = MaterialAlertDialogBuilder(requireContext())
                .setView(binding.root)
                .setTitle(getTitle())
                .setPositiveButton(R.string.title_save) { _, _ -> onSave() }
                .setNegativeButton(R.string.title_cancel) { _, _ -> dismiss() }
                .setNeutralButton(R.string.title_more) { _, _ -> }
                .create()

        //Customize neutral button directly to avoid dialog dismissal on click
        dialog.setOnShowListener {
            dialog.getButton(Dialog.BUTTON_NEUTRAL).setOnClickListener {
                binding.showAll = true
                it.visibility = View.GONE
            }
        }

        return dialog
    }

    /**
     * Returns title string resource
     */
    private fun getTitle() =
            if (profile.ID == 0L) R.string.title_new_server_profile
            else R.string.title_edit_server_profile


    /**
     * Saves current profile.
     */
    private fun onSave() = viewModel.saveProfile(profile)
}