package com.example.anton.sprintcalendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.anton.sprintcalendar.databinding.ActivityMainBinding;

import org.joda.time.LocalDate;

import static com.example.anton.sprintcalendar.PresenceType.FULL_DAY;
import static com.example.anton.sprintcalendar.PresenceType.HALF_DAY;
import static com.example.anton.sprintcalendar.PresenceType.NONE;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    private View summaryView;
    private ActivityMainBinding activityMainBinding;
    private SprintCalendar sprintCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        int weekIds[] = {R.id.firstWeek, R.id.secondWeek};
        int dayIds[] = {R.id.day1, R.id.day2, R.id.day3, R.id.day4, R.id.day5};
        int memberIds[] = {R.id.user1, R.id.user2, R.id.user3, R.id.user4, R.id.user5, R.id.user6};

        attachListenerToAll((ViewGroup) findViewById(R.id.rootView), this);
        summaryView = findViewById(R.id.sprintTotalHours);

        TeamMember pavel = new TeamMember("Pavel");
        pavel.setPresence(new LocalDate(2016, 1, 13), HALF_DAY);
        pavel.setPresence(new LocalDate(2016, 1, 14), NONE);
        TeamMember alex = new TeamMember("Alex");
        alex.setPresence(new LocalDate(2016, 1, 6), NONE);
        Team team = new Team(
                alex,
                new TeamMember("Anton"),
                new TeamMember("Dima"),
                new TeamMember("Paulina"),
                pavel
        );

        sprintCalendar = new SprintCalendar(
                team, new DefaultDateProvider(),
                new DefaultHolidayProvider(new LocalDate(2016, 1, 6))
        );


        sprintCalendar.initByCurrentDate();

        activityMainBinding.setSprintCalendar(sprintCalendar);

    }

    private void attachListenerToAll(ViewGroup rootView, View.OnLongClickListener listener) {
        int childCount = rootView.getChildCount();
        for (int childIndex = 0; childIndex < childCount; childIndex++) {
            View view = rootView.getChildAt(childIndex);
            if (view instanceof ViewGroup) {
                attachListenerToAll((ViewGroup) view, listener);
            } else if (view instanceof TeamMemberDayView) {
                view.setOnLongClickListener(listener);
            }
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return processOnClick(view);
    }

    @Override
    public void onClick(View view) {
        processOnClick(view);
    }

    private boolean processOnClick(View view) {
        if (view instanceof TeamMemberDayView) {
            TeamMemberDayView dayView = (TeamMemberDayView) view;
            TeamMember teamMember = dayView.getTeamMember();
            SprintDay day = dayView.getDay();
            PresenceType presence = teamMember.presence(day.getDate());
            teamMember.setPresence(day.getDate(), presence == NONE ? HALF_DAY : presence == HALF_DAY ? FULL_DAY : NONE);
            dayView.invalidate();
            dayView.requestLayout();
            sprintCalendar.notifyPropertyChanged(com.example.anton.sprintcalendar.BR.totalHours);
            sprintCalendar.notifyPropertyChanged(com.example.anton.sprintcalendar.BR.hoursLeft);
            return true;
        } else {
            return false;
        }
    }

    class mListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            System.out.println("long press " + e);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            System.out.println("onContextClick " + e);
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            System.out.println("onSingleTapUp " + e);
            return true;
        }
    }


}
