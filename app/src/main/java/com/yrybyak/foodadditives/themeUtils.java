package com.yrybyak.foodadditives;

import android.app.Activity;
import android.content.Intent;

public class themeUtils

{
    private static int cTheme;
    public final static int LIGHT = 0;
    public final static int DARK = 1;
    public static void changeToTheme(Activity activity, int theme)

    {
        cTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (cTheme)
        {
            default:
            case LIGHT:
                activity.setTheme(R.style.LightTheme);
                break;
            case DARK:
                activity.setTheme(R.style.DarkTheme);
                break;
        }
    }
}
