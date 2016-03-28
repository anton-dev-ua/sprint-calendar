package net.sourcefusion.agiletools.sprintcalendar

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import net.sourcefusion.agiletools.sprintcalendar.persisting.sugar.SugarTeamRepository
import org.jetbrains.anko.setContentView
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    var ui by Delegates.notNull<CalendarActivityUI>()
    var mainView by Delegates.notNull<View>()

    val holidayProvider by lazy {
        println("lazy holiday provider")
        Injector.holidayProvider
    }

    val sprintCalendar by lazy {
        SprintCalendar(
                SugarTeamRepository(), //TeamMember("John"), TeamMember("Peter")), // TeamMember("Smith"), TeamMember("Susan"), TeamMember("Dario"), TeamMember("Gosha")),
                DefaultDateProvider(),
                holidayProvider
        )
    }

    val teamMemberNameDialogUI by lazy {
        TeamMemberNameDialogUI(this)
    }

    val addTeamMemberDialog by lazy {
        AlertDialog.Builder(this)
                .setTitle("Enter team member name:")
                .setView(teamMemberNameDialogUI.view)
                .setCancelable(false)
                .setPositiveButton("Create", { dialog, which ->
                    val name = teamMemberNameDialogUI.getName()
                    if (name.isNotEmpty() ) {
                        sprintCalendar.addTeamMember(name)
                        mainView.destroyDrawingCache()
                        mainView = ui.setContentView(this)
                    }
                })
                .setNegativeButton("Cancel", { dialog, which ->
                })
                .create();
    }


    val confirmTeamMemberDeleteUI by lazy {
        ConfirmTeamMemberDeleteUI(this)
    }

    val deleteTeamMemberDialog by lazy {
        AlertDialog.Builder(this)
                .setTitle("Are you sure you want to delete team member:")
                .setView(confirmTeamMemberDeleteUI.view)
                .setCancelable(false)
                .setPositiveButton("Delete", { dialog, which ->
                    val teamMember = confirmTeamMemberDeleteUI.teamMember
                    if (teamMember != null) {
                        sprintCalendar.deleteTeamMember(teamMember)
                        mainView.destroyDrawingCache()
                        mainView = ui.setContentView(this)
                    }
                })
                .setNegativeButton("Cancel", { dialog, which ->
                })
                .create();
    }


    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val tag = v.tag;
        if (tag is TeamMember) {
            confirmTeamMemberDeleteUI.teamMember = tag
            menu.setHeaderTitle("Team")
            menu.add(1, 1, 1, "${tag.name} absent all week")
            menu.add(1, 2, 2, "Delete member ${tag.name}")
            menu.add(2, 3, 3, "Add new team member")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        //        return super.onContextItemSelected(item)
        if (item.itemId == 2) {
            deleteTeamMemberDialog.show()
        }
        if (item.itemId == 3) {
            teamMemberNameDialogUI.setName("")
            addTeamMemberDialog.show();
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("activity on create")

        sprintCalendar.initByCurrentDate();

        ui = CalendarActivityUI(sprintCalendar)
        println("$sprintCalendar")
        mainView = ui.setContentView(this)

    }
}

