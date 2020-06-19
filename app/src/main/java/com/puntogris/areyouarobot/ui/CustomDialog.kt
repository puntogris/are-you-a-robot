package com.puntogris.areyouarobot.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.data.Repository
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.utils.Utils

class CustomDialog(val score: Int) : DialogFragment() {
    private val repo = Repository()

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.setView(inflater.inflate(R.layout.dialog_custom, null))
                .setPositiveButton(getString(R.string.save)
                ) { dialog, _ ->

                    (dialog as Dialog)
                        .findViewById<EditText>(R.id.username)
                        .text
                        .toString()
                        .apply {
                            val username: String = if (isEmpty()) Utils.createDefaultRandomName()
                            else this
                            repo.saveScoreFirestore(RankingEntry(score, username))
                    }

                    findNavController().navigate(R.id.welcomeFragment)
                    Toast.makeText(context,getString(R.string.scored_savec_successfully),Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(getString(android.R.string.cancel)
                ) { _, _ ->
                    dialog?.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}