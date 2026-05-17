package com.misw.vinilos.ui.album

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.misw.vinilos.databinding.DialogSuccessBinding

fun Fragment.showSuccessDialog(
    currentDialog: AlertDialog?,
    onDismiss: (AlertDialog) -> Unit
): AlertDialog {
    if (currentDialog?.isShowing == true) return currentDialog

    val dialogBinding = DialogSuccessBinding.inflate(layoutInflater)
    val dialog = MaterialAlertDialogBuilder(requireContext())
        .setView(dialogBinding.root)
        .setCancelable(false)
        .create()

    dialogBinding.btnViewAlbum.setOnClickListener {
        dialog.dismiss()
        findNavController().navigateUp()
    }

    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.show()
    onDismiss(dialog)
    return dialog
}
