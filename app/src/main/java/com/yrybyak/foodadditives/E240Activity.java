package com.yrybyak.foodadditives;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class E240Activity extends Activity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_e240);
    }

    public void ReturnClick(View view) {
        finish();
    }
}