package com.puntogris.areyouarobot.ui.game

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.databinding.SaveRankingDialogBinding
import com.puntogris.areyouarobot.utils.SimpleResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaveRankingDialog : DialogFragment() {

    private val args: SaveRankingDialogArgs by navArgs()
    private val viewModel: SaveRankingViewModel by viewModels()
    private lateinit var binding: SaveRankingDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.save_ranking_dialog,
            null,
            false
        )

        binding.username.setText(viewModel.currentUsername)

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .setPositiveButton(R.string.action_save, null)
            .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
            .create()
            .also {
                it.setOnShowListener { _ ->
                    it.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        onPositiveButtonClicked()
                    }
                }
            }
    }

    private fun onPositiveButtonClicked() {
        lifecycleScope.launch {
            val message = when (viewModel.savePlayerScore(args.score, binding.username.text.toString())) {
                SimpleResult.Failure -> R.string.snack_save_score_error
                SimpleResult.Success -> R.string.snack_save_score_success
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.welcomeFragment)
        }
    }
}