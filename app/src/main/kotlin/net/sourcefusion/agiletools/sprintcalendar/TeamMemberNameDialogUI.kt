package net.sourcefusion.agiletools.sprintcalendar

import android.content.Context
import android.view.View
import android.widget.EditText
import org.jetbrains.anko.*

class TeamMemberNameDialogUI(context: Context) : AnkoComponent<Context> {
    private var nameField: EditText? = null
    val view: View by lazy {
        createView(AnkoContext.create(context))
    }

    override fun createView(ui: AnkoContext<Context>) = with(ui) {
        linearLayout {
            padding = dip(30)
            nameField = editText { textSize = 24f }
        }
    }

    fun getName() = nameField?.text?.toString() ?: ""

    fun setName(name: String) {
        nameField?.setText(name)
    }

}