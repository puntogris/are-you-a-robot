package com.puntogris.areyouarobot.ui

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

class CustomDialog(val score:Int) : DialogFragment() {
    private val repo = Repository()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            builder.setView(inflater.inflate(R.layout.dialog_custom, null))
                .setPositiveButton(getString(R.string.save)
                ) { dialog, _ ->
                    val d = dialog as Dialog
                    val usernameView = d.findViewById<EditText>(R.id.username)
                    var usernameText = usernameView?.text.toString()
                    if (usernameText.isEmpty()) usernameText = Utils.createDefaultRandomName()
                    repo.saveScoreFirestore(RankingEntry(score,usernameText))
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