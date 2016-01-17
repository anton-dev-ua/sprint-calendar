package net.sourcefusion.agiletools.sprintcalendar

import android.R
import android.widget.AnalogClock
import org.jetbrains.anko.*
import kotlin.properties.Delegates

public class SomeActivityUI : AnkoComponent<MainActivity> {

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            textView("Hello")
            textView("Anko")
        }
    }
}