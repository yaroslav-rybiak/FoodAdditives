package com.yrybiak.foodadditives;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

/** Remembers the light/dark theme choice and applies it to the whole app. */
final class ThemeSettings {

    private static final String PREFS = "settings";
    private static final String KEY_NIGHT_MODE = "night_mode";

    private ThemeSettings() {
    }

    /** Applies the saved choice; follows the system setting until the user picks a theme. */
    static void applySaved(Context context) {
        AppCompatDelegate.setDefaultNightMode(prefs(context)
                .getInt(KEY_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
    }

    /** Saves the choice and switches every open screen to it. */
    static void setDark(Context context, boolean dark) {
        int mode = dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO;
        prefs(context).edit().putInt(KEY_NIGHT_MODE, mode).apply();
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
