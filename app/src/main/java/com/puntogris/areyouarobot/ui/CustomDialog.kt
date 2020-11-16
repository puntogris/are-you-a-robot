package com.puntogris.areyouarobot.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.data.Repository
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CustomDialog(val score: Int) : DialogFragment() {
    @Inject lateinit var repo: Repository
    @Inject lateinit var sharedPref: SharedPref

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_custom, null)
            view.findViewById<EditText>(R.id.username).setText(sharedPref.getPlayerName())
            builder.setView(view)
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