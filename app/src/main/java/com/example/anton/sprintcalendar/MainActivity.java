package com.example.anton.sprintcalendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.anton.sprintcalendar.databinding.ActivityMainBinding;

import org.joda.time.LocalDate;

import static com.example.anton.sprintcalendar.AbsenceType.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        TeamMember pavel = new TeamMember("Pavel");
        pavel.addAbsence(new LocalDate(2016, 01, 13), HALF_DAY);
        pavel.addAbsence(new LocalDate(2016, 01, 14), FULL_DAY);
        Team team = new Team(
                new TeamMember("Alex"),
                new TeamMember("Anton"),
                new TeamMember("Dima"),
                new TeamMember("Paulina"),
                pavel
        );

        SprintCalendar sprintCalendar = new SprintCalendar(
                team, new DefaultDateProvider(),
                new DefaultHolidayProvider(new LocalDate(2016, 1, 6))
        );


        sprintCalendar.initByCurrentDate();

        activityMainBinding.setSprintCalendar(sprintCalendar);
        activityMainBinding.setTeam(team);

    }

    public void clickTest(View view) {
        if(view instanceof TeamMemberDayView) {
            TeamMemberDayView teamMemberDayView = (TeamMemberDayView) view;
            TeamMember teamMember = teamMemberDayView.getTeamMember();
            SprintDay day = teamMemberDayView.getDay();
            String message = String.format("team member: %s, day: %s\n", teamMember, day);
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();


//            teamMember.addAbsence(date,
//                   teamMember.presence(date) == 100 ? FULL_DAY : teamMember.presence(date) == 0 ? HALF_DAY : null);
//            ((TeamMemberDayView) view).update();
//            view.invalidate();
//            view.requestLayout();
        }
    }

    @Override
    public void onClick(View v) {
        clickTest(v);
    }
}
