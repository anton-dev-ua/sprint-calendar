package net.sourcefusion.agiletools.sprintcalendar

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.*
import org.joda.time.format.DateTimeFormat
import kotlin.properties.Delegates

class TimerViewUI(context: Context) : AnkoComponent<Context> {
    val view: View by lazy {
        createView(AnkoContext.create(context))
    }

    var startTime = 0L
    val time = 15 * 60 * 1000L
    var timerText: TextView by Delegates.notNull()
    var dialog: Dialog by Delegates.notNull()
    var running = false

    override fun createView(ui: AnkoContext<Context>) = with(ui) {
        verticalLayout {
            linearLayout {
                padding = dip(30)
                timerText = textView {
                    text = "15m 00s"
                    textColor = 0xffffff.opaque
                    textSize = 56f
                    gravity = Gravity.CENTER
                }.lparams {
                    width = matchParent
                    height = wrapContent
                }
            }

            linearLayout {
                padding = dip(30)

                button { text = "Finish" }
                        .onClick {
                            running = false
                            dialog.dismiss()
                        }
            }
        }
    }

    fun start() {
        running = true
        timerText.textColor = 0xffffff.opaque
        setTimerText(time)
        startTime = System.currentTimeMillis()
        val refresher = Handler()
        refresher.postDelayed(
                object : Runnable {
                    override fun run() {
                        val passed = System.currentTimeMillis() - startTime
                        val left = time - passed
                        if (left > 0) {
                            setTimerText(left)
                        } else {
                            setTimerText(-left)
                            timerText.textColor = 0xFF0000.opaque
                        }
                        if(running) {
                            refresher.postDelayed(this, 500)
                        }
                    }

                },
                500
        )
    }


    private fun setTimerText(left: Long) {
        val leftTimeText = DateTimeFormat.forPattern("mm'm' ss's'").print(left)
        timerText.text = leftTimeText
    }
}