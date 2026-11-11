package com.yrybyak.foodadditives;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Insets;
import android.os.Build;
import android.view.View;
import android.view.WindowInsets;

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
        keepContentClearOfSystemBars(activity);
    }

    // Edge-to-edge is enforced when targeting Android 15+, and the Holo window decor no longer
    // insets the content there. Pad the content by whatever part of it is still covered by the
    // status bar, action bar or navigation bar; on older versions this works out to zero.
    private static void keepContentClearOfSystemBars(final Activity activity)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            return;
        }
        final View decor = activity.getWindow().getDecorView();
        final View content = activity.findViewById(android.R.id.content);
        decor.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int l, int t, int r, int b,
                                       int ol, int ot, int or, int ob) {
                WindowInsets windowInsets = decor.getRootWindowInsets();
                if (windowInsets == null) {
                    return;
                }
                Insets bars = windowInsets.getInsets(
                        WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
                int[] location = new int[2];
                // Bottom edge of the action bar in window coordinates; its container already
                // extends up under the status bar, so this covers the status bar as well.
                int actionBarBottom = 0;
                ActionBar actionBar = activity.getActionBar();
                View actionBarContainer = decor.findViewById(activity.getResources()
                        .getIdentifier("action_bar_container", "id", "android"));
                if (actionBar != null && actionBar.isShowing() && actionBarContainer != null) {
                    actionBarContainer.getLocationInWindow(location);
                    actionBarBottom = location[1] + actionBarContainer.getHeight();
                }

                content.getLocationInWindow(location);
                final int left = Math.max(0, bars.left - location[0]);
                final int top = Math.max(0, Math.max(bars.top, actionBarBottom) - location[1]);
                final int right = Math.max(0,
                        bars.right - (decor.getWidth() - location[0] - content.getWidth()));
                final int bottom = Math.max(0,
                        bars.bottom - (decor.getHeight() - location[1] - content.getHeight()));

                if (left != content.getPaddingLeft() || top != content.getPaddingTop()
                        || right != content.getPaddingRight()
                        || bottom != content.getPaddingBottom()) {
                    // Posted because a layout pass is still in progress here.
                    content.post(new Runnable() {
                        @Override
                        public void run() {
                            content.setPadding(left, top, right, bottom);
                        }
                    });
                }
            }
        });
    }
}
