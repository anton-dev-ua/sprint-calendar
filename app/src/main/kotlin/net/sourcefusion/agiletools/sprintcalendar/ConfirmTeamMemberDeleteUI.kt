package net.sourcefusion.agiletools.sprintcalendar

import android.content.Context
import android.view.View
import org.jetbrains.anko.*

class ConfirmTeamMemberDeleteUI(context: Context) : AnkoComponent<Context> {
    var teamMember: TeamMember? = null;
    val view: View by lazy {
        createView(AnkoContext.create(context))
    }

    override fun createView(ui: AnkoContext<Context>) = with(ui) {
        linearLayout {
            padding = dip(30)
            textView {
                text = teamMember?.name
                textSize = 24f
            }
        }
    }
}