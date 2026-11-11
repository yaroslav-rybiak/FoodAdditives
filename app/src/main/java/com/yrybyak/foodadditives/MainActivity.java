package com.yrybyak.foodadditives;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {

    // Only reached on Android 15 and older. On Android 16+ back gestures skip this callback,
    // but the system already moves a root launcher task to the background, so the result is the same.
    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        themeUtils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dark_theme) {
            themeUtils.changeToTheme(this, themeUtils.DARK);
            setContentView(R.layout.activity_main);
        }

        else if (id == R.id.light_theme) {
            themeUtils.changeToTheme(this, themeUtils.LIGHT);
            setContentView(R.layout.activity_main);
        }

        else if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

    public void ButtonE100E199Click(View view) {
        Intent intent = new Intent(MainActivity.this, E100E199Activity.class);
        startActivity(intent);
    }

    public void ButtonE200E299Click(View view) {
        Intent intent = new Intent(MainActivity.this, E200E299Activity.class);
        startActivity(intent);
    }

    public void ButtonE300E399Click(View view) {
        Intent intent = new Intent(MainActivity.this, E300E399Activity.class);
        startActivity(intent);
    }

    public void ButtonE400E499Click(View view) {
        Intent intent = new Intent(MainActivity.this, E400E499Activity.class);
        startActivity(intent);
    }

    public void ButtonE500E599Click(View view) {
        Intent intent = new Intent(MainActivity.this, E500E599Activity.class);
        startActivity(intent);
    }

    public void ButtonE600E699Click(View view) {
        Intent intent = new Intent(MainActivity.this, E600E699Activity.class);
        startActivity(intent);
    }

/*
    public void ButtonE700E799Click(View view) {
        Intent intent = new Intent(MainActivity.this, E700E799Activity.class);
        startActivity(intent);
    }
*/

    public void ButtonE900E999Click(View view) {
        Intent intent = new Intent(MainActivity.this, E900E999Activity.class);
        startActivity(intent);
    }

    public void ButtonE1000E1999Click(View view) {
        Intent intent = new Intent(MainActivity.this, E1000E1999Activity.class);
        startActivity(intent);
    }
}