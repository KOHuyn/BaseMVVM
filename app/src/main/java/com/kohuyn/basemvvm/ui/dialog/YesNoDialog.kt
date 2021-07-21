package com.kohuyn.basemvvm.ui.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import com.core.BaseDialog
import com.kohuyn.basemvvm.databinding.DialogYesNoBinding
import com.utils.ext.clickWithDebounce

/**
 * Created by KOHuyn on 1/29/2021
 */
class YesNoDialog : BaseDialog<DialogYesNoBinding>() {
    override fun getLayoutBinding(): DialogYesNoBinding = DialogYesNoBinding.inflate(layoutInflater)

    private var title: String? = null
    private var message: String? = null
    private var textAccept: String? = null
    private var textCancel: String? = null

    private var onAcceptClick: View.OnClickListener? = null
    private var onCancelClick: View.OnClickListener? = null

    override fun updateUI(savedInstanceState: Bundle?) {
        title?.let { binding.txtTitle.text = it }
        message?.let { binding.txtContent.text = it }
        textCancel?.let { binding.btnCancel.text = it }
        textAccept?.let { binding.btnAccept.text = it }
        binding.btnAccept.setOnClickListener {
            onAcceptClick?.onClick(it)
        }
        binding.btnCancel.setOnClickListener {
            onCancelClick?.onClick(it)
        }
    }

    class Builder(fm: FragmentManager) {
        private val dialog by lazy { YesNoDialog() }

        init {
            dialog.show(fm, YesNoDialog::class.java.simpleName)
        }

        fun setMessage(msg: String): Builder {
            dialog.message = msg
            return this
        }

        fun setTitle(title: String): Builder {
            dialog.title = title
            return this
        }

        fun setButtonCancel(text: String, action: () -> Unit): Builder {
            dialog.run {
                textCancel = text
                onCancelClick = View.OnClickListener {
                    action()
                    dismiss()
                }
            }
            return this
        }

        fun setButtonAccept(text: String, action: () -> Unit): Builder {
            dialog.run {
                textAccept = text
                onAcceptClick = View.OnClickListener {
                    action()
                    dismiss()
                }
            }
            return this
        }
    }
}