package com.yrybiak.foodadditives;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class FoodAdditivesApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeSettings.applySaved(this);
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
