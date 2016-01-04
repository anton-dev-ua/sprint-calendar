package com.example.anton.sprintcalendar;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.anton.sprintcalendar.databinding.ActivityMainBinding;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SprintCalendar sprintCalendar = new SprintCalendar();
        sprintCalendar.initByStartDate(formatter.parseDateTime("18.01.2016").toDate());
        activityMainBinding.setSprintCalendar(sprintCalendar);

    }
}
