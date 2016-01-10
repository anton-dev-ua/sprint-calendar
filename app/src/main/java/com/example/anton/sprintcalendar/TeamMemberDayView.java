package com.example.anton.sprintcalendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class TeamMemberDayView extends View {

    private TeamMember teamMember;
    private SprintDay day;

    public TeamMemberDayView(Context context) {
        super(context);
    }

    public TeamMemberDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TeamMemberDayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TeamMemberDayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getBackground().draw(canvas);
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public void setDay(SprintDay day) {
        this.day = day;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public SprintDay getDay() {
        return day;
    }

    @Override
    public void setBackground(Drawable background) {
        background.setLevel(5000);
        super.setBackground(background);
    }
}
