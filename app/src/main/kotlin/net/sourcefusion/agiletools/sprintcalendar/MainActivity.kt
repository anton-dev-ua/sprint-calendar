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

    private val MENU_WEEK_HOLIDAY = 1;
    private val MENU_DELETE_TEAM_MEMBER = 2;
    private val MENU_ADD_NEW_TEAM_MEMBER = 3;

    var ui by Delegates.notNull<CalendarActivityUI>()
    var mainView by Delegates.notNull<View>()

    val sprintCalendar by lazy {
        SprintCalendar(
                SugarTeamRepository(), //TeamMember("John"), TeamMember("Peter")), // TeamMember("Smith"), TeamMember("Susan"), TeamMember("Dario"), TeamMember("Gosha")),
                Injector.dateProvider,
                Injector.holidayProvider
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
        val pair = v.tag;
        if (pair is Pair<*,*> && pair.first is TeamMember) {
            val member = pair.first as TeamMember
            menu.setHeaderTitle("Team")
            menu.add(1, MENU_WEEK_HOLIDAY, 1, "'${member.name}' absent/present whole week").actionView = v
            menu.add(1, MENU_DELETE_TEAM_MEMBER, 2, "Delete member '${member.name}'").actionView = v
            menu.add(2, MENU_ADD_NEW_TEAM_MEMBER, 3, "Add new team member").actionView = v
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pair = item.actionView?.tag
        if (pair is Pair<*, *> && pair.first is TeamMember && pair.second is Int) {
            val member = pair.first as TeamMember
            val week = pair.second as Int
            when (item.itemId) {
                MENU_WEEK_HOLIDAY -> {
                    sprintCalendar.setAllWeekHolidayFor(member, week)
                }
                MENU_DELETE_TEAM_MEMBER -> {
                    confirmTeamMemberDeleteUI.teamMember = member
                    deleteTeamMemberDialog.show()
                }
                MENU_ADD_NEW_TEAM_MEMBER -> {
                    teamMemberNameDialogUI.setName("")
                    addTeamMemberDialog.show();
                }
            }
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

