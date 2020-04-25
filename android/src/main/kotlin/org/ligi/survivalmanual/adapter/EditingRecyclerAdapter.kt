package org.ligi.survivalmanual.adapter

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.ligi.kaxt.doAfterEdit
import org.ligi.survivalmanual.R
import org.ligi.survivalmanual.viewholder.TextContentViewHolder

class EditingRecyclerAdapter(private val list: MutableList<String>) : RecyclerView.Adapter<TextContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextContentViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(R.layout.edit_text, parent, false) as TextView
        textView.movementMethod = LinkMovementMethod.getInstance()
        return TextContentViewHolder(textView)
    }

    override fun onBindViewHolder(holder: TextContentViewHolder, position: Int) {
        val editText = holder.view as EditText
        editText.setText(list[holder.adapterPosition])

        editText.doAfterEdit {
            list[holder.adapterPosition] = editText.text.toString()
        }
    }

    override fun getItemCount() = list.size

}