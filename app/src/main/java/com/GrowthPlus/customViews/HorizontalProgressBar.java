package com.GrowthPlus.customViews;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.GrowthPlus.R;

public class HorizontalProgressBar extends ConstraintLayout {

    ProgressBar horizontalProgressBar;
    TextView progressBarLevelNumber;

    public HorizontalProgressBar(@NonNull Context context) {
        super(context);
        init(null);
    }

    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public HorizontalProgressBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs){
        inflate(getContext(), R.layout.horizontal_progress_bar, this);
        horizontalProgressBar = findViewById(R.id.horizontalProgressBar);
        progressBarLevelNumber = findViewById(R.id.progressBarLevelNumber);
    }


    public void setBarLevelText (CharSequence text){
        progressBarLevelNumber.setText(text);
    }

    //Note: theres another types of setTextColor method asking for a ColorStateList rather than an int
    public void setBarLevelTextColor (int color){
        progressBarLevelNumber.setTextColor(color);
    }

    //sets the color of the progress bar level number textview (levels 1, 2, 3, and 4 have different colors)
    public void setBarLevelColor (ColorStateList tint){
        progressBarLevelNumber.setBackgroundTintList(tint);
    }

    public void setHorizontalBarColor(ColorStateList list){
        horizontalProgressBar.setProgressTintList(list);
    }

}
