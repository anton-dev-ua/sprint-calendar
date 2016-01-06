package com.example.anton.sprintcalendar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.anton.sprintcalendar.databinding.ActivityMainBinding;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SprintCalendar sprintCalendar = new SprintCalendar(
                new DefaultDateProvider(),
                new DefaultHolidayProvider(new LocalDate(2016, 1, 6))
        );


        sprintCalendar.initByCurrentDate();

        activityMainBinding.setSprintCalendar(sprintCalendar);
        activityMainBinding.setTeam(new Team(
                new TeamMember("Alex"),
                new TeamMember("Anton"),
                new TeamMember("Dima"),
                new TeamMember("Paulina"),
                new TeamMember("Pavel")
        ));

    }

}
