package com.kohuyn.basemvvm.ui.dialog

import android.os.Bundle
import com.core.BaseDialog
import com.kohuyn.basemvvm.databinding.DialogYesNoBinding
import com.utils.ext.clickWithDebounce

/**
 * Created by KOHuyn on 1/29/2021
 */
class YesNoDialog : BaseDialog<DialogYesNoBinding>() {
    override fun getLayoutBinding(): DialogYesNoBinding = DialogYesNoBinding.inflate(layoutInflater)

    override fun updateUI(savedInstanceState: Bundle?) {
        binding.btnAccept.clickWithDebounce {
            toast("ahihi yes")
            dismiss()
        }
        binding.btnCancel.clickWithDebounce {
            toast("ahuhu no")
            dismiss()
        }
    }
}